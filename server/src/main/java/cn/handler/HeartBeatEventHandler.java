package cn.handler;

import cn.api.event.HeartBeatEvent;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 心跳处理器
 *
 * @author nackily
 * @since 1.0.0
 */
public class HeartBeatEventHandler extends SimpleChannelInboundHandler<HeartBeatEvent> {
    private static final Logger logger = LoggerFactory.getLogger(HeartBeatEventHandler.class);
    @Override
    protected void channelRead0(ChannelHandlerContext chc, HeartBeatEvent heartBeatEvent) throws Exception {
        logger.info("收到客户端[{}]的心跳包...", chc.channel().attr(AttributeKey.valueOf("id")).get());
    }
}
