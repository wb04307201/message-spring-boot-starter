package cn.wubo.message.config;

import cn.wubo.message.core.MessageBase;
import cn.wubo.message.core.MessageService;
import cn.wubo.message.exception.MessageRuntimeException;
import cn.wubo.message.page.MessageListServlet;
import cn.wubo.message.record.IMessageRecordService;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.http.HttpServlet;
import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableConfigurationProperties({MessageConfigurationProperties.class})
public class MessageConfiguration {

    private final MessageConfigurationProperties properties;

    public MessageConfiguration(MessageConfigurationProperties properties) {
        this.properties = properties;
    }

    @Bean
    public MessageService messageService(MessageConfigurationProperties properties, List<IMessageRecordService> messageRecordServiceList) {
        IMessageRecordService messageRecordService = messageRecordServiceList.stream().filter(obj -> obj.getClass().getName().equals(properties.getMessageRecord())).findAny().orElseThrow(() -> new MessageRuntimeException(String.format("未找到%s对应的bean，无法加载IMessageRecordService！", properties.getMessageRecord())));
        messageRecordService.init();
        List<MessageBase> aliases = new ArrayList<>();
        aliases.addAll(properties.getDingtalk().getCustomRobot());
        aliases.addAll(properties.getDingtalk().getMessage());
        aliases.addAll(properties.getFeishu().getCustomRobot());
        aliases.addAll(properties.getFeishu().getCustomRobot());
        aliases.addAll(properties.getWeixin().getCustomRobot());
        aliases.addAll(properties.getWeixin().getCustomRobot());
        aliases.addAll(properties.getMail().getSmtp());
        return new MessageService(aliases, messageRecordService);
    }

    @Bean
    public ServletRegistrationBean<HttpServlet> chatBotListServlet(List<IMessageRecordService> messageRecordServiceList) {
        ServletRegistrationBean<HttpServlet> registration = new ServletRegistrationBean<>();
        registration.setServlet(new MessageListServlet(messageRecordServiceList.stream().filter(obj -> obj.getClass().getName().equals(properties.getMessageRecord())).findAny().orElseThrow(() -> new MessageRuntimeException(String.format("未找到%s对应的bean，无法加载IMessageRecordService！", properties.getMessageRecord())))));
        registration.addUrlMappings("/message/list");
        return registration;
    }
}
