package cn.wubo.message.platform.dingtalk;

import cn.wubo.message.core.DingtalkProperties;
import cn.wubo.message.core.MessageType;
import cn.wubo.message.message.MarkdownContent;
import cn.wubo.message.message.TextContent;
import cn.wubo.message.platform.AbstractSendService;
import cn.wubo.message.record.IMessageRecordService;
import cn.wubo.message.util.ContentUtils;
import cn.wubo.message.util.DingtalkUtils;
import com.alibaba.fastjson.JSON;
import com.dingtalk.api.request.OapiRobotSendRequest;
import com.taobao.api.ApiException;
import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.Objects;


@Slf4j
public class DingtalkCustomRobotServiceImpl extends AbstractSendService<DingtalkProperties.CustomRobot> {

    protected DingtalkCustomRobotServiceImpl(IMessageRecordService messageRecordService) {
        super(messageRecordService);
    }

    @Override
    public String sendMarkdown(DingtalkProperties.CustomRobot aliasProperties, MarkdownContent content) {
        OapiRobotSendRequest request = request(content.getContentParams(aliasProperties.getAlias()).getDingtalkCustomRobot());
        request.setMsgtype("markdown");
        OapiRobotSendRequest.Markdown markdown = new OapiRobotSendRequest.Markdown();
        markdown.setTitle(content.getTitle());
        markdown.setText(ContentUtils.toMarkdown(content));
        request.setMarkdown(markdown);
        return execute(aliasProperties, request);
    }

    @Override
    public String sendText(DingtalkProperties.CustomRobot aliasProperties, TextContent content) {
        OapiRobotSendRequest request = request(content.getContentParams(aliasProperties.getAlias()).getDingtalkCustomRobot());
        request.setMsgtype("text");
        OapiRobotSendRequest.Text text = new OapiRobotSendRequest.Text();
        text.setContent(content.getText());
        request.setText(text);
        return execute(aliasProperties, request);
    }

    private OapiRobotSendRequest request(Map<String, Object> params) {
        OapiRobotSendRequest request = new OapiRobotSendRequest();
        OapiRobotSendRequest.At at = new OapiRobotSendRequest.At();
        at.setIsAtAll(params.containsKey("isAll") ? (Boolean) params.get("isAll") : Boolean.TRUE);
        at.setAtMobiles(params.containsKey("atMobiles") ? (List<String>) params.get("atMobiles") : null);
        at.setAtUserIds(params.containsKey("atUserIds") ? (List<String>) params.get("atUserIds") : null);
        request.setAt(at);
        return request;
    }

    private String execute(DingtalkProperties.CustomRobot aliasProperties, OapiRobotSendRequest request) {
        String response;
        try {
            response = JSON.toJSONString(DingtalkUtils.getCustomRobotClient(Objects.requireNonNull(aliasProperties.getAccessToken()), Objects.requireNonNull(aliasProperties.getSecret())).execute(request));
        } catch (ApiException | NoSuchAlgorithmException | UnsupportedEncodingException | InvalidKeyException e) {
            log.error(e.getMessage(), e);
            response = e.getMessage();
        }
        return response;
    }
}
