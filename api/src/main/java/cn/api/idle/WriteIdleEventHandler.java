package cn.api.idle;

import cn.api.event.HeartBeatEvent;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 继承自{@link ChannelInboundHandlerAdapter}。写闲置事件处理器，当一定时间未向对端（服务端）
 * 发送数据时触发。长时间未发送数据，为防止服务器误判当前端（客户端）已掉线，应主动推送一条无业务意
 * 义的心跳包，告诉对端（服务端）自己一切正常。
 *
 * @author nackily
 * @since 1.0.0
 */
public class WriteIdleEventHandler extends ChannelInboundHandlerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(WriteIdleEventHandler.class);

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(evt instanceof IdleStateEvent){
            IdleStateEvent event = (IdleStateEvent) evt;
            if(IdleState.WRITER_IDLE == event.state()){
                logger.info("A heartbeat message is ready to sent...");
                ctx.channel().writeAndFlush(new HeartBeatEvent());
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
