package cn.handler;

import cn.api.event.AuthResponseEvent;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 认证响应处理器
 *
 * @author nackily
 * @since 1.0.0
 */
public class AuthResponseHandler extends SimpleChannelInboundHandler<AuthResponseEvent> {
    private static final Logger logger = LoggerFactory.getLogger(AuthResponseHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext chc, AuthResponseEvent event) throws Exception {
        if (event.isSucceed()) {
            logger.info("客户端认证成功...");
        } else {
            // 认证失败
            logger.error("客户端认证失败：{}", event.getFailMsg());
            logger.info("服务即将停止...");
            chc.channel().close();
            System.exit(0);
        }
    }
}