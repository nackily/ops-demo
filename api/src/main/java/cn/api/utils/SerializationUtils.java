package cn.api.utils;

import cn.api.command.Command;
import cn.api.event.AbstractEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;

/**
 * 序列化工具类
 *
 * @author nackily
 * @since 1.0.0
 */
public final class SerializationUtils {
    private SerializationUtils(){}

    private static final ObjectMapper MAPPER = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

    /**
     * 序列化
     * @param event 对象
     * @return {@link byte[]} 序列化后的字节数组
     * @throws JsonProcessingException 参见 {@link JsonProcessingException}
     */
    public static byte[] serialize(AbstractEvent event) throws JsonProcessingException {
        return MAPPER.writeValueAsBytes(event);
    }

    /**
     * 反序列化
     * @param command 命令
     * @param bytes 原始数据
     * @return {@link Object} 对象
     * @throws IOException IO异常
     */
    public static Object deserialize(byte command, byte[] bytes) throws IOException {
        TypeReference<?> ref = Command.getReferenceByCode(command);
        return MAPPER.readValue(bytes, ref);
    }

    /**
     * 序列化
     * @param o 对象
     * @return {@link byte[]} 序列化后的字符串
     * @throws JsonProcessingException 参见 {@link JsonProcessingException}
     */
    public static String serializeString(Object o) throws JsonProcessingException {
        return MAPPER.writeValueAsString(o);
    }

}
