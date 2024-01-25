package cn.sqlite.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 服务器
 *
 * @author nackily
 * @since 1.0.0
 */
@Getter
@Setter
@ToString
public class Server {

    /**
     * ID
     */
    private Long id;

    /**
     * 服务器名
     */
    private String name;

    /**
     * IP地址
     */
    private String ip;

    /**
     * 在线状态
     */
    private Boolean onlineStatus;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.time.LocalDateTime createTime;

    /**
     * 数据上报间隔时间(s)
     */
    private Integer reportIntervalSeconds;
}
