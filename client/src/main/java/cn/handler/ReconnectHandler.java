package cn.handler;

import cn.skt.Connector;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 断线重连处理器
 *
 * @author nackily
 * @since 1.0.0
 */
public class ReconnectHandler extends ChannelInboundHandlerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(ReconnectHandler.class);

    private final Connector connector;

    public ReconnectHandler(Connector connector) {
        this.connector = connector;
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.error("与服务器的连接已断开...");
        // 连接中途断开后重新连接
        connector.retryConnect(ctx.channel().eventLoop());
        super.channelInactive(ctx);
    }
}