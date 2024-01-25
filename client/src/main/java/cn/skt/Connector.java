package cn.skt;


import cn.api.codec.CustomEventCodec;
import cn.api.codec.CustomFrameDecoder;
import cn.api.idle.WriteIdleEventHandler;
import cn.conf.OpsServerConf;
import cn.handler.AuthResponseHandler;
import cn.handler.DeliverConfigurationHandler;
import cn.handler.ReconnectHandler;
import cn.handler.OrderHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.concurrent.TimeUnit;

/**
 * 连接器
 *
 * @author nackily
 * @since 1.0.0
 */
public final class Connector {
    private Connector(){}
    private static final Logger logger = LoggerFactory.getLogger(Connector.class);

    /**
     * 唯一实例
     */
    private static final Connector INSTANCE = new Connector();

    /**
     * 启动辅助类
     */
    private static final Bootstrap BOOTSTRAP = new Bootstrap();

    static {
        // 线程组
        NioEventLoopGroup group = new NioEventLoopGroup();
        // 启动辅助类配置
        BOOTSTRAP.group(group)
                // 通道类型配置
                .channel(NioSocketChannel.class)
                // 连接超时时间
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, OpsServerConf.CONNECT_TIMEOUT_SECONDS)
                // 保持长连接
                .option(ChannelOption.SO_KEEPALIVE, true)
                // 注册处理器
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel sc) throws Exception {
                        sc.pipeline().addLast(new ReconnectHandler(INSTANCE));          // 断线重连处理器
                        sc.pipeline().addLast(new CustomFrameDecoder());                // 帧解码器
                        sc.pipeline().addLast(new CustomEventCodec());                  // 编解码处理器
                        sc.pipeline().addLast(new LoggingHandler(LogLevel.INFO));       // 日志处理器
                        sc.pipeline().addLast(new IdleStateHandler(0, OpsServerConf.WRITE_IDLE_SECONDS,
                                0, TimeUnit.SECONDS));                        // 写闲置状态监测器
                        sc.pipeline().addLast(new WriteIdleEventHandler());             // 写闲置处理器
                        sc.pipeline().addLast(new AuthResponseHandler());               // 认证响应处理器
                        sc.pipeline().addLast(new OrderHandler());                      // 重启命令处理器
                        sc.pipeline().addLast(new DeliverConfigurationHandler());       // 下发配置处理器
                    }
                });
    }

    /**
     * Get INSTANCE
     * @return Instance
     */
    public static Connector getInstance() {
        return INSTANCE;
    }

    /**
     * 创建连接
     */
    public void doConnect() {
        try {
            // 连接服务器
            ChannelFuture future = BOOTSTRAP
                    // IP 端口
                    .connect(OpsServerConf.SERVER_IP, OpsServerConf.SERVER_PORT)
                    // 连接状态监听器
                    .addListener(new ConnectionStatusListener(this))
                    .sync();
            future.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * 重连
     * @param eventLoop eventLoop
     */
    public void retryConnect(final EventLoop eventLoop)  {
        // 下一次尝试连接延迟时间（s）
        long intervals = OpsServerConf.RETRY_CONNECT_INTERVAL_SECONDS;
        eventLoop.schedule(() -> {
            logger.info("尝试重新连接服务器...");
            doConnect();
        }, intervals, TimeUnit.SECONDS);
    }

}
