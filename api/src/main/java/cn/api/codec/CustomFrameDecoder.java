package cn.api.codec;

import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * 帧解码器，处理半包粘包问题
 *
 * @author nackily
 * @since 1.0.0
 */
public class CustomFrameDecoder extends LengthFieldBasedFrameDecoder {

    /**
     * 由协议约定{@link CustomEventCodec}可知，消息体长度从第 2 个字节开始，到第 5 个字节结束，
     * 共计 4 个字节，能表示的最大值即为 {@code Integer.MAX_VALUE}。
     */
    public CustomFrameDecoder() {
        super(Integer.MAX_VALUE, 1, 4);
    }
}
