package cn.skt;

import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 连接会话
 *
 * @author nackily
 * @since 1.0.0
 */
public class ConnChannelFactory {
    private static final Logger logger = LoggerFactory.getLogger(ConnChannelFactory.class);
    private ConnChannelFactory(){}

    /**
     * 在线数量
     */
    private static final AtomicInteger ONLINE_NUM = new AtomicInteger();

    /**
     * 通道缓存
     */
    private static final Map<Long, Channel> CONTEXT = new ConcurrentHashMap<>();

    /**
     * 注册客户端
     * @param id 身份标识
     * @param channel 通道
     */
    public static void register(Long id, Channel channel) {
        Channel exist = CONTEXT.put(id, channel);
        if (ObjectUtils.isEmpty(exist)) {
            ONLINE_NUM.incrementAndGet();
            logger.info("当前共计有[{}]客户端在线", ONLINE_NUM);
        }
    }

    /**
     * 注销客户端
     * @param channel 通道
     */
    public static void unregister (Channel channel) {
        boolean successful = CONTEXT.entrySet().removeIf(entry -> entry.getValue() == channel);
        if (successful) {
            ONLINE_NUM.decrementAndGet();
            logger.info("当前共计有[{}]客户端在线", ONLINE_NUM);
        }
    }

    /**
     * 获取通道
     * @param id 身份标识
     * @return 通道
     */
    public static Channel getChannel(Long id) {
        return CONTEXT.get(id);
    }
}
