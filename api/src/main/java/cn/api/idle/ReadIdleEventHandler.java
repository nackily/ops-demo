package cn.api.idle;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 继承自{@link ChannelInboundHandlerAdapter}。读闲置事件处理器，当一定时间未收到对端（客户端）
 * 发来数据时触发。长时间未收数据，一般认为是对端（客户端）已掉线。
 *
 * @author nackily
 * @since 1.0.0
 */
public class ReadIdleEventHandler extends ChannelInboundHandlerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(ReadIdleEventHandler.class);

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(evt instanceof IdleStateEvent){
            IdleStateEvent event = (IdleStateEvent) evt;
            if(IdleState.READER_IDLE == event.state()) {
                logger.warn("No data has been received from the client in the past period of time...");
                ctx.channel().close();
                logger.info("Idle client has been closed...");
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
