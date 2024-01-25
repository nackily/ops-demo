package cn.conf;


/**
 * 服务器配置
 *
 * @author nackily
 * @since 1.0.0
 */
public class OpsServerConf {
    private OpsServerConf(){}

    /**
     * 服务器IP
     */
    public static final String SERVER_IP = PropertyLoader.getProperty("ops.server.ip", "127.0.0.1");

    /**
     * 服务器通信端口
     */
    public static final Integer SERVER_PORT = PropertyLoader.getIntProperty("ops.server.port", 8888);

    /**
     * 连接服务器超时时间(s)
     */
    public static final Integer CONNECT_TIMEOUT_SECONDS = PropertyLoader.getIntProperty("ops.server.connect-timeout-seconds", 2);

    /**
     * 重连间隔时间(s)
     */
    public static final Integer RETRY_CONNECT_INTERVAL_SECONDS = PropertyLoader.getIntProperty("ops.server.retry-connect-interval-seconds", 5);

    /**
     * 写闲置时间(s)
     */
    public static final Integer WRITE_IDLE_SECONDS = PropertyLoader.getIntProperty("ops.server.write-idle-seconds", 30);

}
