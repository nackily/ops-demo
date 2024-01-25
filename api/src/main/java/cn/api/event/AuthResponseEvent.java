package cn.api.event;


import cn.api.command.Command;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 认证响应事件
 *
 * @author nackily
 * @since 1.0.0
 */
@Getter
@Setter
@ToString
public class AuthResponseEvent extends AbstractEvent {

    /**
     * 是否认证成功
     */
    private boolean succeed;

    /**
     * 失败原因
     */
    private String failMsg;

    @Override
    public Command getCommand() {
        return Command.AUTH_RSP;
    }
}
