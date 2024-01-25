package cn.api.event;

import cn.api.command.Command;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 消息
 *
 * @author nackily
 * @since 1.0.0
 */
@Getter
@Setter
@ToString
public abstract class AbstractEvent {

    /**
     * 消息唯一标识。当A端发送消息需要同步等待返回时，可给该消息定义一个唯一的消息标识，B端处理数据并返回后将
     * 该消息标识写入到响应中，A端以此作为服务器针对这一条消息的回复。
     */
    private String eventId;

    /**
     * 获取消息对应的命令字节
     * @return {@link byte}
     */
    @JsonIgnore
    public abstract Command getCommand();

}
