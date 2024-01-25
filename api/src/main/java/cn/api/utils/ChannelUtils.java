package cn.api.utils;

import io.netty.channel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Map;

/**
 * ChannelUtils
 *
 * @author nackily
 * @since 1.0.0
 */
public final class ChannelUtils {
    private ChannelUtils(){}
    private static final Logger logger = LoggerFactory.getLogger(ChannelUtils.class);

    /**
     * 检测通道是否可用
     * @param channel channel
     * @return 可用：true
     */
    public static boolean channelAvailable (final Channel channel) {
        return channel.isOpen() && channel.isActive() && channel.isWritable();
    }

    /**
     * 打印所有处理器
     * @param pipeline pipeline
     */
    public static void printAllHandler(final ChannelPipeline pipeline) {
        logger.info("+-------------------------------------------------+");
        logger.info("The following are inbound handler...");
        for (Map.Entry<String, ChannelHandler> next : pipeline) {
            if (next.getValue() instanceof ChannelInboundHandler) {
                logger.info("IN :: name:[{}], handler class:[{}]", next.getKey(), next.getValue().getClass());
            }
        }
        logger.info("The following are outbound handler...");
        for (Map.Entry<String, ChannelHandler> next : pipeline) {
            if (next.getValue() instanceof ChannelOutboundHandler) {
                logger.info("OUT :: name:[{}], handler class:[{}]", next.getKey(), next.getValue().getClass());
            }
        }
        logger.info("+-------------------------------------------------+");
    }
}
