package cn.sqlite.mapper;

import cn.sqlite.entity.Server;
import org.apache.ibatis.annotations.*;

import java.util.Collection;
import java.util.List;

/**
 * ServerMapper
 *
 * @author nackily
 * @since 1.0.0
 */
@Mapper
public interface ServerMapper {

    /**
     * IP-in查询
     * @param ips IPs
     * @return 列表
     */
    @Select({"<script>",
                "select * from server ",
                "where ip in ",
                    "<foreach collection='ips' item='ip' open='(' separator=',' close=')'>",
                        "#{ip}",
                    "</foreach>",
            "</script>"})
    List<Server> findByIps (@Param(value = "ips") Collection<String> ips);

    /**
     * ID查找
     * @param id ID
     * @return 记录
     */
    @Select("select * from server where id = #{id}")
    Server findById(@Param(value = "id") Long id);

    /**
     * 更新服务器状态
     * @param id ID
     * @param online 状态
     * @return 受影响行数
     */
    @Update("update server set online_status = #{online} where id = #{id}")
    int updateStatus(@Param(value = "id")Long id, @Param(value = "status")Boolean online);

    /**
     * 更新服务器上报数据间隔时间
     * @param id ID
     * @param ris 时间间隔
     * @return 受影响行数
     */
    @Update("update server set report_interval_seconds = #{ris} where id = #{id}")
    int updateReportIntervalSeconds(@Param(value = "id")Long id, @Param(value = "ris")Integer ris);

    /**
     * 插入服务器
     * @param server 服务器
     * @return 受影响的行数
     */
    @Insert("insert into server(name, ip, online_status, create_time, report_interval_seconds) " +
            "values (#{name}, #{ip}, #{onlineStatus}, #{createTime}, #{reportIntervalSeconds})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int insert(Server server);
}
