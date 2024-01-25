package cn.api.event;

import cn.api.command.Command;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.math.BigDecimal;
import java.util.List;

/**
 * 指标事件
 *
 * @author nackily
 * @since 1.0.0
 */
@Getter
@Setter
@ToString
public class MetricsEvent extends AbstractEvent {

    @Override
    public Command getCommand() {
        return Command.UP_METRICS;
    }

    /**
     * 操作系统
     */
    private String osName;

    /**
     * 系统架构
     */
    private String osArch;

    /**
     * 系统信息
     */
    private String os;

    /**
     * CPU当前等待率(%)
     */
    private BigDecimal cpuWaitRate;

    /**
     * CPU空闲率(%)
     */
    private BigDecimal cpuIdleRate;

    /**
     * CPU系统使用率(%)
     */
    private BigDecimal cpuOsUsageRate;

    /**
     * CPU用户使用率(%)
     */
    private BigDecimal cpuUserUsageRate;

    /**
     * CPU使用率(%)
     */
    private BigDecimal cpuUsageRate;

    /**
     * CPU核数(个)
     */
    private Integer cpuProcessorCount;

    /**
     * 总内存(GB)
     */
    private BigDecimal memoryTotal;

    /**
     * 内存已用(GB)
     */
    private BigDecimal memoryUsed;

    /**
     * 内存使用率(%)
     */
    private BigDecimal memoryUsageRate;

    /**
     * 总磁盘(GB)
     */
    private BigDecimal diskTotal;

    /**
     * 磁盘已用(GB)
     */
    private BigDecimal diskUsed;

    /**
     * 磁盘使用率(%)
     */
    private BigDecimal diskUsageRate;

    /**
     * 磁盘使用情况
     */
    private List<DiskUsage> diskUsages;

    /**
     * 网口列表
     */
    private List<NetworkInterface> networkInterfaces;

    /**
     * 上报时间
     */
    private java.time.LocalDateTime reportTime;


    /**
     * 磁盘使用情况
     *
     * @author nackily
     * @since 1.0.0
     */
    @Getter
    @Setter
    @ToString
    public static class DiskUsage {

        /**
         * 磁盘名称
         */
        private String spaceName;

        /**
         * 磁盘总空间(GB)
         */
        private BigDecimal totalSize;

        /**
         * 已使用(GB)
         */
        private BigDecimal usedSize;

        /**
         * 使用率(%)
         */
        private BigDecimal usedRate;

        /**
         * 可用(GB)
         */
        private BigDecimal availableSize;
    }

    /**
     * 网络接口
     *
     * @author nackily
     * @since 1.0.0
     */
    @Getter
    @Setter
    @ToString
    public static class NetworkInterface {

        /**
         * 名称
         */
        private String name;

        /**
         * 显示名称
         */
        private String displayName;

        /**
         * MAC地址
         */
        private String macAddr;

        /**
         * ipv4地址
         */
        private String ipv4Addr;

        /**
         * ipv6地址
         */
        private String ipv6Addr;

        /**
         * 接收字节数
         */
        private Long bytesRecv;

        /**
         * 发送字节数
         */
        private Long bytesSent;

        /**
         * 接收数据包
         */
        private Long packetsRecv;

        /**
         * 发送数据包
         */
        private Long packetsSent;

        /**
         * 速度(Mbps)
         */
        private BigDecimal speed;
    }
}
