package cn.api.event;

import cn.api.command.Command;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.util.List;

/**
 * 认证请求事件
 *
 * @author nackily
 * @since 1.0.0
 */
@Getter
@Setter
@ToString
public class AuthRequestEvent extends AbstractEvent {

    /**
     * IP列表
     */
    private List<String> ips;

    @Override
    public Command getCommand() {
        return Command.AUTH_REQ;
    }
}
