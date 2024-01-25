package cn.handler;

import cn.skt.ConnChannelFactory;
import cn.sqlite.mapper.ServerMapper;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

/**
 * 清理会话处理器
 *
 * @author nackily
 * @since 1.0.0
 */
@ChannelHandler.Sharable
@Component
public class ClearSessionHandler extends ChannelInboundHandlerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(ClearSessionHandler.class);

    @Autowired
    private ServerMapper mapper;

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Attribute<Long> idAttr = ctx.channel().attr(AttributeKey.valueOf("id"));
        if (!ObjectUtils.isEmpty(idAttr.get())) {
            Long serverId = idAttr.get();
            logger.warn("客户端[{}]已断开连接...", serverId);
            ConnChannelFactory.unregister(ctx.channel());
            // 更新服务器在线状态
            mapper.updateStatus(serverId, false);
        }
        super.channelInactive(ctx);
    }
}