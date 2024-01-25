package cn.api.codec;


import cn.api.event.AbstractEvent;
import cn.api.utils.SerializationUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

/**
 * 自定义编解码器，数据传输约定格式如下：
 * <p>
 *     命令(1字节) + 消息体长度(4字节) + 消息体(N字节)
 * </p>
 *
 * @author nackily
 * @since 1.0.0
 */
public class CustomEventCodec extends ByteToMessageCodec<AbstractEvent> {
    private static final Logger logger = LoggerFactory.getLogger(CustomEventCodec.class);

    /**
     * 编码，负责将对象解析成数据
     * @param channelHandlerContext 通道上下文
     * @param abstractEvent 待出栈消息
     * @param byteBuf 将写入编码消息的 ByteBuf
     * @throws Exception 异常
     */
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, AbstractEvent abstractEvent, ByteBuf byteBuf) throws Exception {
        // 1个字节的命令
        byte command = abstractEvent.getCommand().getCode();
        byteBuf.writeByte(command);
        // 序列化
        byte[] body = SerializationUtils.serialize(abstractEvent);
        // 4个字节的消息体长度
        byteBuf.writeInt(body.length);
        // n个字节的消息体
        byteBuf.writeBytes(body);
        logger.info("出站消息已完成编码：{}", abstractEvent);
    }

    /**
     * 解码，负责将数据解析成对象
     * @param channelHandlerContext 通道上下文
     * @param byteBuf 从中读取数据的 ByteBuf
     * @param out 应添加解码消息的列表
     * @throws Exception 异常
     */
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> out) throws Exception {
        // 字节命令
        byte command = byteBuf.readByte();
        // 消息体长度：丢弃
        byteBuf.readInt();
        // 消息体
        byte[] body = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(body);
        // 反序列化
        Object event = SerializationUtils.deserialize(command, body);
        out.add(event);
        logger.info("入站消息已完成解码：{}", event);
    }
}
