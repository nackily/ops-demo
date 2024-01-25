package cn.handler;

import cn.api.event.DeliverConfigurationEvent;
import cn.scheduled.MetricsUploadTask;
import cn.scheduled.TaskScheduledThreadPoolExecutor;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * 下发配置处理器
 *
 * @author nackily
 * @since 1.0.0
 */
public class DeliverConfigurationHandler extends SimpleChannelInboundHandler<DeliverConfigurationEvent> {
    private static final Logger logger = LoggerFactory.getLogger(DeliverConfigurationHandler.class);

    /**
     * 指标采集任务
     */
    private static ScheduledFuture<?> metricsFuture;

    @Override
    protected void channelRead0(ChannelHandlerContext chc, DeliverConfigurationEvent event) throws Exception {
        logger.info("收到服务器下发的配置：{}", event);
        synchronized (DeliverConfigurationHandler.class) {
            if (metricsFuture != null && !metricsFuture.isCancelled()) {
                metricsFuture.cancel(false);
                logger.info("已取消指标采集任务...");
            }
            metricsFuture = TaskScheduledThreadPoolExecutor.getInstance().scheduleWithFixedDelay(
                    new MetricsUploadTask(chc.channel()),
                    0,
                    event.getReportIntervalSeconds(),
                    TimeUnit.SECONDS);
            logger.info("已向线程池提交指标采集任务...");
        }
    }
}