package cn.api.event;


import cn.api.command.Command;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 下发配置
 *
 * @author nackily
 * @since 1.0.0
 */
@Getter
@Setter
@ToString
public class DeliverConfigurationEvent extends AbstractEvent {
    @Override
    public Command getCommand() {
        return Command.DL_CONFIG;
    }

    /**
     * 上报间隔时间(s)
     */
    private Integer reportIntervalSeconds;
}
