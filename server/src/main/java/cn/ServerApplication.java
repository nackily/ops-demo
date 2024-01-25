package cn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 启动类
 *
 * @author nackily
 * @since 1.0.0
 */
@SpringBootApplication
public class ServerApplication {

    /**
     * 启用类入口
     * @param args args
     */
    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }
}
