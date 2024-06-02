package cn.wubo.message.config;

import cn.wubo.message.core.MessageBase;
import cn.wubo.message.core.MessageService;
import cn.wubo.message.exception.MessageRuntimeException;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableConfigurationProperties({MessageConfigurationProperties.class})
public class MessageConfiguration {

    /**
     * 创建并初始化消息服务。
     * 这个方法根据配置属性来决定使用哪个消息记录服务（IMessageRecordService），
     * 并聚合各种消息通道（如钉钉、飞书、微信、邮件等）的配置信息，用于之后的消息发送服务。
     *
     * @param properties 消息配置属性，包含各种消息通道的配置信息，以及指定的消息记录服务名称。
     * @return 初始化后的消息服务实例，用于发送消息和记录消息发送情况。
     * @throws MessageRuntimeException 如果根据配置属性未能找到对应的消息记录服务实例，则抛出异常。
     */
    @Bean
    public MessageService messageService(MessageConfigurationProperties properties) {
        // 集合所有消息别名，包括钉钉、飞书、微信、邮件等通道的消息配置
        List<MessageBase> aliases = new ArrayList<>();
        aliases.addAll(properties.getDingtalk().getCustomRobot());
        aliases.addAll(properties.getDingtalk().getMessage());
        aliases.addAll(properties.getFeishu().getCustomRobot());
        aliases.addAll(properties.getFeishu().getMessage());
        aliases.addAll(properties.getWeixin().getCustomRobot());
        aliases.addAll(properties.getWeixin().getMessage());
        aliases.addAll(properties.getMail().getSmtp());

        // 返回初始化好的消息服务实例
        return new MessageService(aliases);
    }
}
