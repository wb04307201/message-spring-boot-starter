package cn.wubo.message.core;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 钉钉
 */
@Data
public class DingtalkProperties {

    private List<CustomRobot> customRobot = new ArrayList<>();

    private List<Message> message = new ArrayList<>();

    /**
     * 自定义机器人
     * https://open.dingtalk.com/document/group/custom-robot-access
     * https://open.dingtalk.com/document/orgapp/custom-bot-to-send-group-chat-messages
     */
    @Data
    public static class CustomRobot extends MessageBase {
        private String accessToken;
        private String secret;
    }

    /**
     * 工作通知
     * https://open.dingtalk.com/document/orgapp/asynchronous-sending-of-enterprise-session-messages
     */
    @Data
    public static class Message extends MessageBase {
        private String appkey;
        private String appsecret;
        private Long agentId;
    }
}
