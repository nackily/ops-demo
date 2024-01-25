package cn.scheduled;

import cn.api.event.MetricsEvent;
import cn.utils.OSHIUtils;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDateTime;

/**
 * 指标收集上传任务
 *
 * @author nackily
 * @since 1.0.0
 */
public class MetricsUploadTask implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(MetricsUploadTask.class);

    private final Channel channel;

    public MetricsUploadTask(Channel channel) {
        this.channel = channel;
    }

    @Override
    public void run() {
        logger.info("开始收集本机指标...");
        MetricsEvent event = new MetricsEvent();
        OSHIUtils.setOsInfo(event);                       // 系统
        OSHIUtils.setCpuInfo(event);                      // CPU
        OSHIUtils.setMemoryInfo(event);                   // 内存
        OSHIUtils.setDiskInfo(event);                     // 硬盘
        OSHIUtils.setNetworkInfo(event);                  // 网卡
        event.setReportTime(LocalDateTime.now());

        channel.writeAndFlush(event);
    }
}
