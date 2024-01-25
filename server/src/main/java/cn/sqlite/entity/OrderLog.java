package cn.sqlite.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.time.LocalDateTime;

/**
 * 指令记录
 *
 * @author nackily
 * @since 1.0.0
 */
@Getter
@Setter
@ToString
public class OrderLog {

    /**
     * ID
     */
    private Long id;

    /**
     * 服务器ID
     */
    private Long serverId;

    /**
     * 指令内容
     */
    private String orderContent;

    /**
     * 下发时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime issueTime;

    /**
     * 下发状态
     */
    private Boolean issueSucceeded;

    /**
     * 下发错误描述
     */
    private String issueError;

    /**
     * 执行时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime executeTime;

    /**
     * 执行状态
     */
    private Boolean executeSucceeded;

    /**
     * 执行错误描述
     */
    private String executeError;

    /**
     * 执行输出
     */
    private String executeOut;

    /**
     * 上报时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime reportTime;
}
