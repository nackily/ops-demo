package cn.handler;

import cn.api.event.OrderEvent;
import cn.api.event.OrderResultEvent;
import cn.utils.ProcessUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.internal.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDateTime;

/**
 * 指令处理器
 *
 * @author nackily
 * @since 1.0.0
 */
public class OrderHandler extends SimpleChannelInboundHandler<OrderEvent> {
    private static final Logger logger = LoggerFactory.getLogger(OrderHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext chc, OrderEvent event) throws Exception {
        logger.info("收到服务器下发的指令：{}", event);
        OrderResultEvent res;
        if (StringUtil.isNullOrEmpty(event.getOrderContent())) {
            res = new OrderResultEvent();
            res.setExecuteSucceeded(false);
            res.setExecuteError("空指令");
        } else {
            res = ProcessUtils.exec(event.getOrderContent());
        }
        res.setOrderLogId(event.getOrderLogId());
        res.setReportTime(LocalDateTime.now());
        chc.channel().writeAndFlush(res);
    }
}
