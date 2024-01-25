package cn.api.event;

import cn.api.command.Command;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 指令事件
 *
 * @author nackily
 * @since 1.0.0
 */
@Getter
@Setter
@ToString
public class OrderEvent extends AbstractEvent {
    @Override
    public Command getCommand() {
        return Command.DL_ORDER;
    }

    /**
     * 记录ID
     */
    private Long orderLogId;

    /**
     * 指令内容
     */
    private String orderContent;
}
