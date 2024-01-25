package cn.api.command;

import cn.api.event.*;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.Getter;
import java.util.NoSuchElementException;

/**
 * 命令字节常量定义
 *
 * @author nackily
 * @since 1.0.0
 */
@Getter
public enum Command {

    /**
     * 认证请求
     */
    AUTH_REQ((byte) 0x01, new TypeReference<AuthRequestEvent>(){}),

    /**
     * 认证响应
     */
    AUTH_RSP((byte) 0x02, new TypeReference<AuthResponseEvent>(){}),

    /**
     * 心跳
     */
    HEARTBEAT((byte) 0x03, new TypeReference<HeartBeatEvent>(){}),



    /**
     * 下发配置
     */
    DL_CONFIG((byte) 0x11, new TypeReference<DeliverConfigurationEvent>(){}),

    /**
     * 下发指令
     */
    DL_ORDER((byte) 0x12, new TypeReference<OrderEvent>(){}),



    /**
     * 上报服务器指标
     */
    UP_METRICS((byte) 0x21, new TypeReference<MetricsEvent>(){}),

    /**
     * 上报指令执行结果
     */
    UP_ORDER_RESULT((byte) 0x22, new TypeReference<OrderResultEvent>(){});

    /**
     * 命令码
     */
    private final byte code;

    /**
     * 命令实体类型
     */
    private final TypeReference<?> ref;
    Command(byte code, TypeReference<?> ref) {
        this.code = code;
        this.ref = ref;
    }


    /**
     * 获取实体类型
     * @param command 命令
     * @return 结果
     */
    public static TypeReference<?> getReferenceByCode(byte command) {
        for (Command item : Command.values()) {
            if (item.code == command) {
                return item.ref;
            }
        }
        throw new NoSuchElementException("未知命令");
    }
}