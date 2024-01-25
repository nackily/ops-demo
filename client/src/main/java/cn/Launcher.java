package cn;

import cn.skt.Connector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 启动类
 *
 * @author nackily
 * @since 1.0.0
 */
public class Launcher {
    private static final Logger logger = LoggerFactory.getLogger(Launcher.class);

    public static void main(String[] args) {
        try {
            // 启动Netty客户端
            Connector.getInstance().doConnect();
        } catch (Exception e) {
            logger.error("启动连接异常：", e);
        }
    }

}
