package cn.utils;

import cn.api.event.OrderResultEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * ProcessUtils
 *
 * @author nackily
 * @since 1.0.0
 */
public class ProcessUtils {
    private static final Logger logger = LoggerFactory.getLogger(ProcessUtils.class);
    private ProcessUtils(){}

    /**
     * 执行命令
     * @param command 命令
     * @return 结果
     */
    public static OrderResultEvent exec(String command) {
        OrderResultEvent event = new OrderResultEvent();
        logger.info("即将执行指令：{}", command);
        try {
            // 执行脚本
            Process prs = Runtime.getRuntime().exec(command);
            // 执行时间
            event.setExecuteTime(LocalDateTime.now());
            // 收集执行输出
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(prs.getInputStream(), Charset.forName("GBK")));
            List<String> outs = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                outs.add(line);
            }
            event.setExecuteOut(String.join("\n", outs));
            // 收集执行状态
            event.setExecuteSucceeded(prs.waitFor() == 0);
            logger.info("指令执行完成");

        } catch (IOException | InterruptedException e) {
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            event.setExecuteSucceeded(false);
            event.setExecuteError(e.getMessage());
            logger.error("执行指令出错：", e);
        }
        return event;
    }
}
