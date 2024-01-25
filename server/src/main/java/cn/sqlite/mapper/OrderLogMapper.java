package cn.sqlite.mapper;

import cn.api.event.OrderResultEvent;
import cn.sqlite.entity.OrderLog;
import org.apache.ibatis.annotations.*;

/**
 * OrderLogMapper
 *
 * @author nackily
 * @since 1.0.0
 */
@Mapper
public interface OrderLogMapper {


    /**
     * 插入指令记录
     * @param orderLog 指令记录
     * @return 受影响的行数
     */
    @Insert("insert into order_log(server_id, order_content, issue_time, issue_succeeded, issue_error, execute_time, " +
                "execute_succeeded, execute_error, execute_out, report_time) " +
            "values (#{serverId}, #{orderContent}, #{issueTime}, #{issueSucceeded}, #{issueError}, #{executeTime}, " +
                "#{executeSucceeded}, #{executeError}, #{executeOut}, #{reportTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int insert(OrderLog orderLog);


    /**
     * 更新指令执行情况
     * @param orderEvent 指令事件
     * @return 受影响的行数
     */
    @Update("update order_log set execute_time = #{executeTime}, execute_succeeded = #{executeSucceeded}, " +
                "execute_error = #{executeError}, execute_out = #{executeOut}, report_time = #{reportTime} " +
            "where id = #{orderLogId} ")
    int updateExecute(OrderResultEvent orderEvent);

    /**
     * ID查找
     * @param id ID
     * @return 记录
     */
    @Select("select * from order_log where id = #{id}")
    OrderLog findById(@Param(value = "id") Long id);
}
