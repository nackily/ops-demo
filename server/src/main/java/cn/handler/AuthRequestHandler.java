package cn.handler;

import cn.api.event.AuthRequestEvent;
import cn.api.event.AuthResponseEvent;
import cn.api.event.DeliverConfigurationEvent;
import cn.api.utils.ChannelUtils;
import cn.skt.ConnChannelFactory;
import cn.sqlite.entity.Server;
import cn.sqlite.mapper.ServerMapper;
import cn.util.SpringBeanUtils;
import io.netty.channel.*;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import java.util.List;

/**
 * 认证响应处理器
 *
 * @author nackily
 * @since 1.0.0
 */
@ChannelHandler.Sharable
@Component
public class AuthRequestHandler extends SimpleChannelInboundHandler<AuthRequestEvent> {
    private static final Logger logger = LoggerFactory.getLogger(AuthRequestHandler.class);

    @Autowired
    private ServerMapper mapper;

    @Override
    protected void channelRead0(ChannelHandlerContext chc, AuthRequestEvent event) throws Exception {
        logger.info("收到客户端的认证请求：{}", event);
        Channel channel = chc.channel();
        if (CollectionUtils.isEmpty(event.getIps())) {
            writeFail(channel, "未发现可用的的IP");
            return;
        }
        // IP白名单认证
        List<Server> servers = mapper.findByIps(event.getIps());
        if (CollectionUtils.isEmpty(servers)) {
            writeFail(channel, "未发现受信任的IP");
            return;
        }
        if (servers.size() > 1) {
            writeFail(channel, "匹配到多个受信任的IP");
            return;
        }
        Server server = servers.get(0);
        // 连接绑定ID属性
        AttributeKey<Long> key = AttributeKey.valueOf("id");
        channel.attr(key).set(server.getId());
        // 绑定会话
        ConnChannelFactory.register(server.getId(), channel);

        // 注册业务处理器
        reRegisterHandler(channel.pipeline());

        // 推送认证结果
        AuthResponseEvent rsp = new AuthResponseEvent();
        rsp.setSucceed(true);
        channel.writeAndFlush(rsp);
        // 推送服务器配置
        DeliverConfigurationEvent dce = new DeliverConfigurationEvent();
        dce.setReportIntervalSeconds(server.getReportIntervalSeconds());
        channel.writeAndFlush(dce);

        // 更新服务器状态
        mapper.updateStatus(server.getId(), true);
    }


    /**
     * 重新注册处理器
     * @param pipeline pipeline
     */
    protected void reRegisterHandler(ChannelPipeline pipeline) {
        // 通过认证后，移除认证处理器，添加业务处理器
        pipeline.remove(this);
        pipeline.addLast(new HeartBeatEventHandler());                              // 心跳处理器
        pipeline.addLast(SpringBeanUtils.getBean(MetricsHandler.class));            // 指标上报处理器
        pipeline.addLast(SpringBeanUtils.getBean(OrderResultHandler.class));      // 重启命令执行结果处理器

        ChannelUtils.printAllHandler(pipeline);
    }

    /**
     * 响应失败消息
     * @param channel 通道
     * @param failMsg 失败原因
     */
    private void writeFail(Channel channel, String failMsg) {
        AuthResponseEvent rsp = new AuthResponseEvent();
        rsp.setSucceed(false);
        rsp.setFailMsg(failMsg);
        channel.writeAndFlush(rsp);
    }
}