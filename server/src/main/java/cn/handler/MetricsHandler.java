package cn.handler;

import cn.api.event.MetricsEvent;
import cn.sqlite.entity.MetricsLog;
import cn.sqlite.mapper.MetricsLogMapper;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 指标上传处理器
 *
 * @author nackily
 * @since 1.0.0
 */
@ChannelHandler.Sharable
@Component
public class MetricsHandler extends SimpleChannelInboundHandler<MetricsEvent> {
    private static final Logger logger = LoggerFactory.getLogger(MetricsHandler.class);

    @Autowired
    private MetricsLogMapper metricsLogMapper;

    @Override
    protected void channelRead0(ChannelHandlerContext chc, MetricsEvent event) throws Exception {
        AttributeKey<Long> ak = AttributeKey.valueOf("id");
        Long serverId = chc.channel().attr(ak).get();
        logger.info("收到客户端[{}]上传的指标：{}", serverId, event);
        // 保存指标
        MetricsLog metricsLog = MetricsLog.from(event);
        metricsLog.setServerId(serverId);
        metricsLogMapper.insert(metricsLog);
    }

}
