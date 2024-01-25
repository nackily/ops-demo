package cn.utils;

import cn.api.event.MetricsEvent;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.NetworkIF;
import oshi.software.os.OSFileStore;
import oshi.software.os.OperatingSystem;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * OSHI工具类
 *
 * @author nackily
 * @since 1.0.0
 */
public class OSHIUtils {
    private OSHIUtils(){}

    /**
     * B 换算 GB，保留两位小数
     * @param bytes 字节
     * @return GB
     */
    public static BigDecimal b2Gb(long bytes) {
        double gbFormat = 1024 * 1024 * 1024.0;
        return BigDecimal.valueOf(bytes / gbFormat)
                .setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * 计算百分比，保留两位小数
     * @param part 部分量
     * @param total 全量
     * @return 百分比
     */
    public static BigDecimal getPercent(long part, long total) {
        return BigDecimal.valueOf(part * 1.0 / total)
                .multiply(BigDecimal.valueOf(100))
                .setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * 写入系统信息
     * @param o 对象
     */
    public static void setOsInfo(MetricsEvent o) {
        Properties props = System.getProperties();
        // 系统名称
        o.setOsName(props.getProperty("os.name"));
        // 架构名称
        o.setOsArch(props.getProperty("os.arch"));
        // 系统信息
        OperatingSystem os = new SystemInfo().getOperatingSystem();
        o.setOs(os.toString());
    }

    /**
     * 写入CPU信息
     * @param o 对象
     */
    public static void setCpuInfo(MetricsEvent o) {
        CentralProcessor processor = new SystemInfo().getHardware().getProcessor();
        long[] prevTicks = processor.getSystemCpuLoadTicks();
        // 睡眠1s，取样
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
        long[] ticks = processor.getSystemCpuLoadTicks();
        long user = ticks[CentralProcessor.TickType.USER.getIndex()] - prevTicks[CentralProcessor.TickType.USER.getIndex()];
        long nice = ticks[CentralProcessor.TickType.NICE.getIndex()] - prevTicks[CentralProcessor.TickType.NICE.getIndex()];
        long sys = ticks[CentralProcessor.TickType.SYSTEM.getIndex()] - prevTicks[CentralProcessor.TickType.SYSTEM.getIndex()];
        long idle = ticks[CentralProcessor.TickType.IDLE.getIndex()] - prevTicks[CentralProcessor.TickType.IDLE.getIndex()];
        long wait = ticks[CentralProcessor.TickType.IOWAIT.getIndex()] - prevTicks[CentralProcessor.TickType.IOWAIT.getIndex()];
        long irq = ticks[CentralProcessor.TickType.IRQ.getIndex()] - prevTicks[CentralProcessor.TickType.IRQ.getIndex()];
        long softIrq = ticks[CentralProcessor.TickType.SOFTIRQ.getIndex()] - prevTicks[CentralProcessor.TickType.SOFTIRQ.getIndex()];
        long steal = ticks[CentralProcessor.TickType.STEAL.getIndex()] - prevTicks[CentralProcessor.TickType.STEAL.getIndex()];

        long total = user + nice + sys + idle + wait + irq + softIrq + steal;
        // 核数
        o.setCpuProcessorCount(processor.getLogicalProcessorCount());
        // 系统使用率
        o.setCpuOsUsageRate(OSHIUtils.getPercent(sys, total));
        // 用户使用率
        o.setCpuUserUsageRate(OSHIUtils.getPercent(user, total));
        // 当前等待率
        o.setCpuWaitRate(OSHIUtils.getPercent(wait, total));
        // 空闲率
        BigDecimal idleRate = OSHIUtils.getPercent(idle, total);
        o.setCpuIdleRate(idleRate);
        // 使用率
        o.setCpuUsageRate(BigDecimal.valueOf(100).subtract(idleRate));
    }


    /**
     * 写入内存信息
     * @param o 对象
     */
    public static void setMemoryInfo(MetricsEvent o) {
        GlobalMemory memory = new SystemInfo().getHardware().getMemory();
        // 总内存
        long total = memory.getTotal();
        o.setMemoryTotal(OSHIUtils.b2Gb(total));
        // 已用内存
        long used = total - memory.getAvailable();
        o.setMemoryUsed(OSHIUtils.b2Gb(used));
        // 内存使用率
        o.setMemoryUsageRate(OSHIUtils.getPercent(used, total));
    }

    /**
     * 写入磁盘信息
     * @param o 对象
     */
    public static void setDiskInfo(MetricsEvent o) {
        List<OSFileStore> fileStores = new SystemInfo().getOperatingSystem().getFileSystem().getFileStores();
        List<MetricsEvent.DiskUsage> diskUsages = new ArrayList<>();
        long totalDiskSize = 0;
        long usedDiskSize = 0;
        for (OSFileStore item : fileStores) {
            MetricsEvent.DiskUsage disk = new MetricsEvent.DiskUsage();
            // 磁盘名称
            disk.setSpaceName(item.getName());
            // 总空间
            disk.setTotalSize(OSHIUtils.b2Gb(item.getTotalSpace()));
            // 已用空间
            long used = item.getTotalSpace() - item.getUsableSpace();
            disk.setUsedSize(OSHIUtils.b2Gb(used));
            // 使用率
            disk.setUsedRate(OSHIUtils.getPercent(used, item.getTotalSpace()));
            // 可用空间
            disk.setAvailableSize(OSHIUtils.b2Gb(item.getUsableSpace()));
            diskUsages.add(disk);
            totalDiskSize += item.getTotalSpace();
            usedDiskSize += used;
        }
        o.setDiskUsages(diskUsages);
        // 磁盘总容量
        o.setDiskTotal(OSHIUtils.b2Gb(totalDiskSize));
        // 磁盘总占用
        o.setDiskUsed(OSHIUtils.b2Gb(usedDiskSize));
        // 磁盘使用率
        o.setDiskUsageRate(OSHIUtils.getPercent(usedDiskSize, totalDiskSize));
    }

    /**
     * 写入网卡信息
     * @param o 对象
     */
    public static void setNetworkInfo(MetricsEvent o) {
        List<NetworkIF> networkIfs = new SystemInfo().getHardware().getNetworkIFs();
        List<MetricsEvent.NetworkInterface> networkInterfaces = new ArrayList<>();
        for (NetworkIF item : networkIfs) {
            MetricsEvent.NetworkInterface ni = new MetricsEvent.NetworkInterface();
            // 网卡名称
            ni.setName(item.getName());
            // 显示名称
            ni.setDisplayName(item.getDisplayName());
            // MAC地址
            ni.setMacAddr(item.getMacaddr());
            // ipv4、ipv6地址
            ni.setIpv4Addr(Arrays.toString(item.getIPv4addr()));
            ni.setIpv6Addr(Arrays.toString(item.getIPv6addr()));
            // 发送/接收字节数
            ni.setBytesRecv(item.getBytesRecv());
            ni.setBytesSent(item.getBytesSent());
            // 发送/接收数据包
            ni.setPacketsRecv(item.getPacketsRecv());
            ni.setPacketsSent(item.getPacketsSent());
            // 速度（Mbps）
            BigDecimal mbps = BigDecimal.valueOf(item.getSpeed() / 1000000.0)
                    .setScale(2, RoundingMode.HALF_UP);
            ni.setSpeed(mbps);
            networkInterfaces.add(ni);
        }
        o.setNetworkInterfaces(networkInterfaces);
    }

    /**
     * 获取Ipv4列表
     * @return 结果
     */
    public static List<String> getIpv4s() {
        return new SystemInfo().getHardware().getNetworkIFs()
                .stream()
                .filter(o -> !(o.getIPv4addr() == null || o.getIPv4addr().length == 0))
                .map(o -> String.join("", o.getIPv4addr()))
                .collect(Collectors.toList());
    }

}
