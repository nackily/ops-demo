package cn.skt;

import cn.api.codec.CustomEventCodec;
import cn.api.codec.CustomFrameDecoder;
import cn.api.idle.ReadIdleEventHandler;
import cn.handler.AuthRequestHandler;
import cn.handler.ClearSessionHandler;
import cn.util.SpringBeanUtils;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;
import java.util.concurrent.TimeUnit;

/**
 * 服务启动器
 *
 * @author nackily
 * @since 1.0.0
 */
@Component
public class NettyBootstrapRunner implements ApplicationRunner, ApplicationListener<ContextClosedEvent> {
    private static final Logger logger = LoggerFactory.getLogger(NettyBootstrapRunner.class);

    /**
     * 服务端口
     */
    @Value("${ops.server.port:8888}")
    private int port;

    /**
     * 写闲置时间(s)
     */
    @Value("${ops.server.read-idle-seconds:30}")
    private int readIdleSeconds;

    private Channel serverChannel;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup worker = new NioEventLoopGroup();
        try {
            serverChannel = new ServerBootstrap()
                    .channel(NioServerSocketChannel.class)
                    .group(boss, worker)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel sc) throws Exception {
                            sc.pipeline().addLast(SpringBeanUtils.getBean(ClearSessionHandler.class));  // 清理会话处理器
                            sc.pipeline().addLast(new CustomFrameDecoder());                            // 帧解码器
                            sc.pipeline().addLast(new CustomEventCodec());                              // 编解码处理器
                            sc.pipeline().addLast(new LoggingHandler(LogLevel.INFO));                   // 日志处理器
                            sc.pipeline().addLast(new IdleStateHandler(readIdleSeconds,
                                    0, 0, TimeUnit.SECONDS));                    // 读闲置状态监测
                            sc.pipeline().addLast(new ReadIdleEventHandler());                          // 读闲置处理器
                            sc.pipeline().addLast(SpringBeanUtils.getBean(AuthRequestHandler.class));   // 认证处理器
                        }
                    })
                    .bind(port)
                    .sync()
                    .channel();
            serverChannel.closeFuture().sync();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }

    }

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        if (serverChannel != null) {
            serverChannel.close();
        }
        logger.info("Socket服务已停止...");
    }

}
