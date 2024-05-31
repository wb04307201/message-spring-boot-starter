package cn.wubo.message.config;

import cn.wubo.message.core.MessageBase;
import cn.wubo.message.core.MessageService;
import cn.wubo.message.exception.MessageRuntimeException;
import cn.wubo.message.record.IMessageRecordService;
import cn.wubo.message.record.MessageRecord;
import cn.wubo.message.record.impl.MemMessageRecordServiceImpl;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.servlet.function.*;
import org.springframework.web.util.HtmlUtils;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

@Configuration
@EnableConfigurationProperties({MessageConfigurationProperties.class})
public class MessageConfiguration {

    @Bean
    public IMessageRecordService messageRecordService() {
        return new MemMessageRecordServiceImpl();
    }

    /**
     * 创建并初始化消息服务。
     * 这个方法根据配置属性来决定使用哪个消息记录服务（IMessageRecordService），
     * 并聚合各种消息通道（如钉钉、飞书、微信、邮件等）的配置信息，用于之后的消息发送服务。
     *
     * @param properties 消息配置属性，包含各种消息通道的配置信息，以及指定的消息记录服务名称。
     * @param messageRecordServiceList 消息记录服务的列表，通过这个列表找到与配置属性中指定名称相匹配的服务实例。
     * @return 初始化后的消息服务实例，用于发送消息和记录消息发送情况。
     * @throws MessageRuntimeException 如果根据配置属性未能找到对应的消息记录服务实例，则抛出异常。
     */
    @Bean
    public MessageService messageService(MessageConfigurationProperties properties, List<IMessageRecordService> messageRecordServiceList) {
        // 根据配置属性找到对应的消息记录服务实例，如果找不到则抛出异常
        IMessageRecordService messageRecordService = messageRecordServiceList.stream().filter(obj -> obj.getClass().getName().equals(properties.getMessageRecord())).findAny().orElseThrow(() -> new MessageRuntimeException(String.format("未找到%s对应的bean，无法加载IMessageRecordService！", properties.getMessageRecord())));
        messageRecordService.init(); // 初始化消息记录服务

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
        return new MessageService(aliases, messageRecordService);
    }

    /**
     * 创建一个消息路由功能 Bean。
     *
     * @param messageService 消息服务，用于处理消息记录的列表查询和其它业务逻辑。
     * @return RouterFunction<ServerResponse> 一个路由功能对象，负责处理客户端请求并返回响应。
     */
    @Bean("wb04307201MessageRouter")
    public RouterFunction<ServerResponse> messageRouter(MessageService messageService) {
        // 定义一个二元函数，处理 GET 和 POST 请求，并构建相应的 ServerResponse。
        BiFunction<ServerRequest, MessageService, ServerResponse> biFunction = (request, service) -> {
            String contextPath = request.requestPath().contextPath().value(); // 获取当前请求的上下文路径
            Map<String, Object> data = new HashMap<>(); // 用于存储要传递给模板的数据
            MessageRecord messageRecord = new MessageRecord(); // 创建一个消息记录对象

            try {
                // 处理 POST 请求，从请求参数中获取并设置消息记录的属性
                if (HttpMethod.POST.equals(request.method())) {
                    MultiValueMap<String, String> params = request.params();
                    messageRecord.setAlias(params.getFirst("alias"));
                    messageRecord.setType(params.getFirst("type"));
                    messageRecord.setContent(params.getFirst("content"));
                    messageRecord.setResponse(params.getFirst("response"));
                }

                // 查询消息记录列表，并将结果和其它信息放入数据映射中
                data.put("list", service.list(messageRecord));
                data.put("contextPath", contextPath);
                // 对消息内容和响应进行HTML转义，防止XSS攻击
                messageRecord.setContent(HtmlUtils.htmlEscape(messageRecord.getContent() == null ? "" : messageRecord.getContent()));
                messageRecord.setResponse(HtmlUtils.htmlEscape(messageRecord.getResponse() == null ? "" : messageRecord.getResponse()));
                data.put("query", messageRecord);

                // 使用 FreeMarker 模板引擎处理数据并生成 HTML 响应
                StringWriter sw = new StringWriter();
                freemarker.template.Configuration cfg = new freemarker.template.Configuration(freemarker.template.Configuration.VERSION_2_3_23);
                cfg.setClassForTemplateLoading(this.getClass(), "/template");
                Template template = cfg.getTemplate("list.ftl", "UTF-8");
                template.process(data, sw);
                // 构建并返回包含HTML响应的 ServerResponse
                return ServerResponse.ok().contentType(MediaType.TEXT_HTML).body(sw.toString());
            } catch (IOException | TemplateException e) {
                // 处理模板处理或IO异常，抛出自定义的消息运行时异常
                throw new MessageRuntimeException(e.getMessage(), e);
            }
        };

        // 构建路由规则，处理 GET 和 POST 请求
        return RouterFunctions.route().GET("/message/list", RequestPredicates.accept(MediaType.TEXT_HTML), request -> biFunction.apply(request, messageService))
                .POST("/message/list", RequestPredicates.accept(MediaType.APPLICATION_FORM_URLENCODED), request -> biFunction.apply(request, messageService)).build();
    }
}
