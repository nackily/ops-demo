package cn.sqlite.mapper;

import cn.sqlite.entity.MetricsLog;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

/**
 * MetricsLogMapper
 *
 * @author nackily
 * @since 1.0.0
 */
@Mapper
public interface MetricsLogMapper {


    /**
     * 插入指标
     * @param metricsLog 指标
     * @return 受影响的行数
     */
    @Insert("insert into metrics_log(server_id, os_name, os_arch, os, cpu_wait_rate, cpu_idle_rate, " +
                "cpu_os_usage_rate, cpu_user_usage_rate, cpu_usage_rate, cpu_processor_count, memory_total, " +
                "memory_used, memory_usage_rate, disk_total, disk_used, disk_usage_rate, report_time, " +
                "net_if, disk_usage) " +
            "values (#{serverId}, #{osName}, #{osArch}, #{os}, #{cpuWaitRate}, #{cpuIdleRate}, " +
                "#{cpuOsUsageRate}, #{cpuUserUsageRate}, #{cpuUsageRate}, #{cpuProcessorCount}, #{memoryTotal}, " +
                "#{memoryUsed}, #{memoryUsageRate}, #{diskTotal}, #{diskUsed}, #{diskUsageRate}, #{reportTime}, " +
                "#{netIf}, #{diskUsage})")
    int insert(MetricsLog metricsLog);
}
