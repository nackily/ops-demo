package cn.controller;

import cn.api.event.DeliverConfigurationEvent;
import cn.api.event.OrderEvent;
import cn.api.utils.ChannelUtils;
import cn.skt.ConnChannelFactory;
import cn.sqlite.entity.OrderLog;
import cn.sqlite.entity.Server;
import cn.sqlite.mapper.OrderLogMapper;
import cn.sqlite.mapper.ServerMapper;
import io.netty.channel.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;

/**
 * ServerController
 *
 * @author nackily
 * @since 1.0.0
 */
@RestController
@RequestMapping("/server")
public class ServerController {

    @Autowired
    private ServerMapper serverMapper;

    @Autowired
    private OrderLogMapper orderLogMapper;

    /**
     * 添加服务器
     * @param server 服务器信息
     * @return 受影响行数
     */
    @PostMapping("/add")
    public int add(@RequestBody Server server) {
        server.setCreateTime(LocalDateTime.now());
        if (ObjectUtils.isEmpty(server.getReportIntervalSeconds())) {
            server.setReportIntervalSeconds(300);
        }
        server.setOnlineStatus(false);
        return serverMapper.insert(server);
    }

    /**
     * 重置上报时间间隔
     * @param id 服务器ID
     * @param val 新值
     * @return 受影响行数
     */
    @GetMapping("/reset/reportIntervalSeconds/{id}/{val}")
    public int resetReportIntervalSeconds(@PathVariable(value = "id") Long id,
                                          @PathVariable(value = "val") Integer val) {
        Server s = serverMapper.findById(id);
        if (s.getReportIntervalSeconds().longValue() == val) {
            return 0;
        }
        // 下发参数事件
        Channel channel = ConnChannelFactory.getChannel(id);
        if (! ObjectUtils.isEmpty(channel)) {
            DeliverConfigurationEvent event = new DeliverConfigurationEvent();
            event.setReportIntervalSeconds(val);
            channel.writeAndFlush(event);
        }
        return serverMapper.updateReportIntervalSeconds(id, val);
    }

    /**
     * 执行指令
     * @param id 服务器ID
     * @param order 指令
     * @return 受影响行数
     */
    @PostMapping("/exec/order/{id}")
    public int exec(@PathVariable(value = "id") Long id, @RequestBody String order) {
        OrderLog log = new OrderLog();
        log.setServerId(id);
        log.setOrderContent(order);
        log.setIssueTime(LocalDateTime.now());

        Channel channel = ConnChannelFactory.getChannel(id);
        if (null == channel) {
            log.setIssueSucceeded(false);
            log.setIssueError("客户端离线");
            return orderLogMapper.insert(log);
        } else {
            if (ChannelUtils.channelAvailable(channel)) {
                log.setIssueSucceeded(true);
                int result = orderLogMapper.insert(log);
                OrderEvent event = new OrderEvent();
                event.setOrderContent(order);
                event.setOrderLogId(log.getId());
                channel.writeAndFlush(event);
                return result;
            } else {
                log.setIssueSucceeded(false);
                log.setIssueError("客户端连接不可用");
                return orderLogMapper.insert(log);
            }
        }
    }
}
