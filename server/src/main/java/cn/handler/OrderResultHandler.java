package cn.handler;

import cn.api.event.OrderResultEvent;
import cn.sqlite.mapper.OrderLogMapper;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 执行执行结果处理器
 *
 * @author nackily
 * @since 1.0.0
 */
@ChannelHandler.Sharable
@Component
public class OrderResultHandler extends SimpleChannelInboundHandler<OrderResultEvent> {
    private static final Logger logger = LoggerFactory.getLogger(OrderResultHandler.class);

    @Autowired
    private OrderLogMapper mapper;

    @Override
    protected void channelRead0(ChannelHandlerContext chc, OrderResultEvent event) throws Exception {
        logger.info("收到客户端[{}]的指令执行结果：{}", chc.channel().attr(AttributeKey.valueOf("id")).get(), event);
        mapper.updateExecute(event);
    }
}