package cn.api.event;


import cn.api.command.Command;
import lombok.ToString;

/**
 * 心跳包
 *
 * @author nackily
 * @since 1.0.0
 */
@ToString
public class HeartBeatEvent extends AbstractEvent {
    @Override
    public Command getCommand() {
        return Command.HEARTBEAT;
    }
}
