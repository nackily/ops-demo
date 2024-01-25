package cn.skt;

import cn.api.event.AuthRequestEvent;
import cn.utils.OSHIUtils;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 连接状态监听器
 *
 * @author nackily
 * @since 1.0.0
 */
public class ConnectionStatusListener implements ChannelFutureListener {
    private static final Logger logger = LoggerFactory.getLogger(ConnectionStatusListener.class);

    private final Connector connector;
    public ConnectionStatusListener(Connector connector) {
        this.connector = connector;
    }

    @Override
    public void operationComplete(ChannelFuture cf) {
        if (cf.isSuccess()) {
            logger.info("连接服务器成功...");
            // 连接成功，发起认证
            AuthRequestEvent event = new AuthRequestEvent();
            event.setIps(OSHIUtils.getIpv4s());
            cf.channel().writeAndFlush(event);
        } else {
            logger.error("连接服务器失败：", cf.cause());
            // 连接失败，尝试再次连接
            connector.retryConnect(cf.channel().eventLoop());
        }
    }

}
