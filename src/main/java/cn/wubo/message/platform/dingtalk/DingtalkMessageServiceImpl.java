package cn.wubo.message.platform.dingtalk;

import cn.wubo.message.core.DingtalkProperties;
import cn.wubo.message.exception.DingtalkRuntimeException;
import cn.wubo.message.message.MarkdownContent;
import cn.wubo.message.message.TextContent;
import cn.wubo.message.platform.AbstractSendService;
import cn.wubo.message.util.ContentUtils;
import com.alibaba.fastjson2.JSON;
import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request;

import java.util.Map;
import java.util.Objects;

public class DingtalkMessageServiceImpl extends AbstractSendService<DingtalkProperties.Message> {

    @Override
    public String sendMarkdown(DingtalkProperties.Message aliasProperties, MarkdownContent content) {
        OapiMessageCorpconversationAsyncsendV2Request request = request(aliasProperties, content.getContentParams(aliasProperties.getAlias()).getDingtalkMessage());
        OapiMessageCorpconversationAsyncsendV2Request.Msg msg = new OapiMessageCorpconversationAsyncsendV2Request.Msg();
        msg.setMsgtype("markdown");
        OapiMessageCorpconversationAsyncsendV2Request.Markdown markdown = new OapiMessageCorpconversationAsyncsendV2Request.Markdown();
        markdown.setTitle(content.getTitle());
        markdown.setText(ContentUtils.toMarkdown(content));
        msg.setMarkdown(markdown);
        request.setMsg(msg);
        return execute(aliasProperties, request);
    }

    @Override
    public String sendText(DingtalkProperties.Message aliasProperties, TextContent content) {
        OapiMessageCorpconversationAsyncsendV2Request request = request(aliasProperties, content.getContentParams(aliasProperties.getAlias()).getDingtalkMessage());
        OapiMessageCorpconversationAsyncsendV2Request.Msg msg = new OapiMessageCorpconversationAsyncsendV2Request.Msg();
        msg.setMsgtype("text");
        OapiMessageCorpconversationAsyncsendV2Request.Text text = new OapiMessageCorpconversationAsyncsendV2Request.Text();
        text.setContent(content.getText());
        msg.setText(text);
        request.setMsg(msg);
        return execute(aliasProperties, request);
    }

    private OapiMessageCorpconversationAsyncsendV2Request request(DingtalkProperties.Message aliasProperties, Map<String, Object> params) {
        OapiMessageCorpconversationAsyncsendV2Request request = new OapiMessageCorpconversationAsyncsendV2Request();
        request.setAgentId(aliasProperties.getAgentId());
        request.setToAllUser(params.containsKey("to_all_user") ? (Boolean) params.get("to_all_user") : null);
        request.setUseridList(params.containsKey("userid_list") ? (String) params.get("userid_list") : null);
        request.setDeptIdList(params.containsKey("dept_id_list") ? (String) params.get("dept_id_list") : null);
        return request;
    }

    /**
     * 执行钉钉消息发送。
     * 该方法封装了向钉钉发送消息的逻辑，通过调用钉钉开放平台的API，将请求参数转换为JSON字符串返回。
     * 如果在执行过程中遇到异常，将抛出自定义的DingtalkRuntimeException异常。
     *
     * @param aliasProperties 钉钉配置属性，包含应用的appkey和appsecret，用于身份验证。
     * @param request         发送消息的请求对象，包含了消息的接收者、内容等信息。
     * @return 返回处理结果的JSON字符串。
     * @throws DingtalkRuntimeException 如果执行过程中发生异常，将抛出此运行时异常。
     */
    private String execute(DingtalkProperties.Message aliasProperties, OapiMessageCorpconversationAsyncsendV2Request request) {
        try {
            // @formatter:off
            // 调用DingtalkUtils的getMessageClient方法获取消息客户端实例
            // 然后使用该实例执行请求，并传入通过钉钉应用的appkey和appsecret获取的访问令牌
            // 最后将执行结果使用JSON.toJSONString方法转换为JSON字符串返回
            return JSON.toJSONString(
                    DingtalkUtils.getMessageClient()
                            .execute(
                                    request,
                                    DingtalkUtils.getToken(
                                            Objects.requireNonNull(aliasProperties.getAppkey()),
                                            Objects.requireNonNull(aliasProperties.getAppsecret())
                                    )
                            )
            );
            // @formatter:on
        } catch (Exception e) {
            // 当执行过程中发生异常时，抛出自定义的运行时异常DingtalkRuntimeException
            // 异常信息包含原始异常的消息和原始异常本身
            throw new DingtalkRuntimeException(e.getMessage(), e);
        }
    }
}
