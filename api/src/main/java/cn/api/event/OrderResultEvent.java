package cn.api.event;

import cn.api.command.Command;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 指令执行结果上报事件
 *
 * @author nackily
 * @since 1.0.0
 */
@Getter
@Setter
@ToString
public class OrderResultEvent extends AbstractEvent {
    @Override
    public Command getCommand() {
        return Command.UP_ORDER_RESULT;
    }

    /**
     * 记录ID
     */
    private Long orderLogId;

    /**
     * 指令执行时间
     */
    private java.time.LocalDateTime executeTime;

    /**
     * 指令执行是否成功
     */
    private boolean executeSucceeded;

    /**
     * 指令执行输出
     */
    private String executeOut;

    /**
     * 指令执行错误信息
     */
    private String executeError;

    /**
     * 指令上报时间
     */
    private java.time.LocalDateTime reportTime;
}
