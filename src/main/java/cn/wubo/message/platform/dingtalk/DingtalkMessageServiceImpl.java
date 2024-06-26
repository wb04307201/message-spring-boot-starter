package cn.wubo.message.platform.dingtalk;

import cn.wubo.message.core.DingtalkProperties;
import cn.wubo.message.exception.DingtalkRuntimeException;
import cn.wubo.message.message.MarkdownContent;
import cn.wubo.message.message.TextContent;
import cn.wubo.message.platform.AbstractSendService;
import cn.wubo.message.util.ContentUtils;
import com.alibaba.fastjson2.JSON;
import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request;
import com.dingtalk.api.response.OapiGettokenResponse;

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

    private String execute(DingtalkProperties.Message aliasProperties, OapiMessageCorpconversationAsyncsendV2Request request) {
        try {
            OapiGettokenResponse oapiGettokenResponse = DingtalkUtils.getToken(Objects.requireNonNull(aliasProperties.getAppkey()), Objects.requireNonNull(aliasProperties.getAppsecret()));
            return JSON.toJSONString(DingtalkUtils.getMessageClient().execute(request, oapiGettokenResponse.getAccessToken()));
        } catch (Exception e) {
            throw new DingtalkRuntimeException(e.getMessage(), e);
        }
    }
}
