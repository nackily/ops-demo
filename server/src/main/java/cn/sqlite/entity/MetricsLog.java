package cn.sqlite.entity;

import cn.api.event.MetricsEvent;
import cn.api.utils.SerializationUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.BeanUtils;

import java.io.IOException;

/**
 * 指标记录
 *
 * @author nackily
 * @since 1.0.0
 */
@Getter
@Setter
@ToString
public class MetricsLog extends MetricsEvent {

    /**
     * ID
     */
    private Long id;

    /**
     * 服务器ID
     */
    private Long serverId;

    /**
     * 网卡信息
     */
    private String netIf;

    /**
     * 硬盘信息
     */
    private String diskUsage;


    public static MetricsLog from(MetricsEvent ev) throws IOException {
        MetricsLog m = new MetricsLog();
        BeanUtils.copyProperties(ev, m);
        m.setNetIf(SerializationUtils.serializeString(ev.getNetworkInterfaces()));
        m.setDiskUsage(SerializationUtils.serializeString(ev.getDiskUsages()));
        return m;
    }
}
