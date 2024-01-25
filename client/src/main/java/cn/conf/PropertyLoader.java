package cn.conf;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Properties;

/**
 * 资源配置加载器
 * <P>
 * ConfigLoader借助于{@link Properties}实现，默认加载 application.yml 配置文件中的所有配置，以UTF-8编码格式读取。
 * </P>
 *
 * @see Charset
 * @author nackily
 * @since 1.0.0
 */
public final class PropertyLoader {
    private PropertyLoader(){}

    /**
     * 已加载的配置存储在当前{@link Properties}中。
     */
    private static final Properties CONFIG_PROPERTIES = new Properties();
    static {
        try {
            CONFIG_PROPERTIES.load(new InputStreamReader(
                    Objects.requireNonNull(PropertyLoader.class.getResourceAsStream("/application.properties")),
                    StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取配置
     * @param configKey 配置Key
     * @param defaultVal 默认值
     * @return 值
     */
    public static String getProperty(String configKey, String defaultVal) {
        return CONFIG_PROPERTIES.getProperty(configKey, defaultVal);
    }

    /**
     * 获取配置
     * @param configKey 配置Key
     * @return 值
     */
    public static String getProperty(String configKey) {
        return CONFIG_PROPERTIES.getProperty(configKey);
    }

    /**
     * 获取配置
     * @param configKey 配置Key
     * @param defaultVal 默认值
     * @return 值
     */
    public static Integer getIntProperty(String configKey, Integer defaultVal) {
        String p = CONFIG_PROPERTIES.getProperty(configKey);
        return (p == null) ? defaultVal : Integer.valueOf(p);
    }

    /**
     * 获取配置
     * @param configKey 配置Key
     * @return 值
     */
    public static Integer getIntProperty(String configKey) {
        String p = CONFIG_PROPERTIES.getProperty(configKey);
        return Integer.valueOf(p);
    }

}
