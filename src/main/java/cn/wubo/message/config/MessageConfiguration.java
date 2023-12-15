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

    @Bean
    public MessageService messageService(MessageConfigurationProperties properties, List<IMessageRecordService> messageRecordServiceList) {
        IMessageRecordService messageRecordService = messageRecordServiceList.stream().filter(obj -> obj.getClass().getName().equals(properties.getMessageRecord())).findAny().orElseThrow(() -> new MessageRuntimeException(String.format("未找到%s对应的bean，无法加载IMessageRecordService！", properties.getMessageRecord())));
        messageRecordService.init();
        List<MessageBase> aliases = new ArrayList<>();
        aliases.addAll(properties.getDingtalk().getCustomRobot());
        aliases.addAll(properties.getDingtalk().getMessage());
        aliases.addAll(properties.getFeishu().getCustomRobot());
        aliases.addAll(properties.getFeishu().getMessage());
        aliases.addAll(properties.getWeixin().getCustomRobot());
        aliases.addAll(properties.getWeixin().getMessage());
        aliases.addAll(properties.getMail().getSmtp());
        return new MessageService(aliases, messageRecordService);
    }

    @Bean("wb04307201MessageRouter")
    public RouterFunction<ServerResponse> messageRouter(MessageService messageService) {
        BiFunction<ServerRequest, MessageService, ServerResponse> biFunction = (request, service) -> {
            String contextPath = request.requestPath().contextPath().value();
            Map<String, Object> data = new HashMap<>();
            MessageRecord messageRecord = new MessageRecord();
            try {
                if (HttpMethod.POST.equals(request.method())) {
                    MultiValueMap<String, String> params = request.params();
                    messageRecord.setAlias(params.getFirst("alias"));
                    messageRecord.setType(params.getFirst("type"));
                    messageRecord.setContent(params.getFirst("content"));
                    messageRecord.setResponse(params.getFirst("response"));
                }
                data.put("list", service.list(messageRecord));
                data.put("contextPath", contextPath);
                messageRecord.setContent(HtmlUtils.htmlEscape(messageRecord.getContent() == null ? "" : messageRecord.getContent()));
                messageRecord.setResponse(HtmlUtils.htmlEscape(messageRecord.getResponse() == null ? "" : messageRecord.getResponse()));
                data.put("query", messageRecord);
                StringWriter sw = new StringWriter();
                freemarker.template.Configuration cfg = new freemarker.template.Configuration(freemarker.template.Configuration.VERSION_2_3_23);
                cfg.setClassForTemplateLoading(this.getClass(), "/template");
                Template template = cfg.getTemplate("list.ftl", "UTF-8");
                template.process(data, sw);
                return ServerResponse.ok().contentType(MediaType.TEXT_HTML).body(sw.toString());
            } catch (IOException | TemplateException e) {
                throw new MessageRuntimeException(e.getMessage(), e);
            }
        };
        return RouterFunctions.route().GET("/message/list", RequestPredicates.accept(MediaType.TEXT_HTML), request -> biFunction.apply(request, messageService)).POST("/message/list", RequestPredicates.accept(MediaType.APPLICATION_FORM_URLENCODED), request -> biFunction.apply(request, messageService)).build();
    }
}
