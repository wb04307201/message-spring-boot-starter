package cn.wubo.message.platform.dingtalk;

import cn.wubo.message.core.DingtalkProperties;
import cn.wubo.message.exception.DingtalkRuntimeException;
import cn.wubo.message.message.MarkdownContent;
import cn.wubo.message.message.TextContent;
import cn.wubo.message.platform.AbstractSendService;
import cn.wubo.message.util.ContentUtils;
import com.alibaba.fastjson2.JSON;
import com.dingtalk.api.request.OapiRobotSendRequest;
import com.taobao.api.ApiException;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class DingtalkCustomRobotServiceImpl extends AbstractSendService<DingtalkProperties.CustomRobot> {

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
        at.setIsAtAll(params.containsKey("isAll") ? (Boolean) params.get("isAll") : null);
        at.setAtMobiles(params.containsKey("atMobiles") ? (List<String>) params.get("atMobiles") : null);
        at.setAtUserIds(params.containsKey("atUserIds") ? (List<String>) params.get("atUserIds") : null);
        request.setAt(at);
        return request;
    }

    /**
     * 执行钉钉自定义机器人发送消息的操作。
     * 该方法封装了向钉钉自定义机器人发送消息的过程，通过提供的access token和secret验证身份，并发送请求。
     * 如果在执行过程中出现异常，将会抛出DingtalkRuntimeException。
     *
     * @param aliasProperties 钉钉自定义机器人配置属性，包含access token和secret。
     * @param request 发送给钉钉自定义机器人的消息请求对象。
     * @return 返回钉钉服务器响应的JSON字符串。
     * @throws DingtalkRuntimeException 如果执行过程中出现异常，将会抛出此运行时异常。
     */
    private String execute(DingtalkProperties.CustomRobot aliasProperties, OapiRobotSendRequest request) {
        try {
            // @formatter:off
            // 使用JSON.toJSONString序列化钉钉客户端执行结果为JSON字符串
            return JSON.toJSONString(
                    DingtalkUtils
                            .getCustomRobotClient(
                                    Objects.requireNonNull(aliasProperties.getAccessToken()),
                                    Objects.requireNonNull(aliasProperties.getSecret())
                            )
                            .execute(request)
            );
            // @formatter:on
        } catch (ApiException | NoSuchAlgorithmException | InvalidKeyException e) {
            // 捕获异常并抛出自定义的运行时异常，以便上层调用者能够更方便地处理异常情况
            throw new DingtalkRuntimeException(e.getMessage(), e);
        }
    }
}
