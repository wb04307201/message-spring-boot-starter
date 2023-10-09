package cn.wubo.message.core;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class FeishuProperties {

    private List<DingtalkProperties.CustomRobot> customRobot = new ArrayList<>();

    private List<DingtalkProperties.Message> message = new ArrayList<>();

    /**
     * 自定义机器人
     * https://open.feishu.cn/document/client-docs/bot-v3/add-custom-bot
     */
    @Data
    public static class CustomRobot extends MessageBase {
        private String hookid;
        private String secret;
    }

    /**
     * 消息
     * https://open.feishu.cn/document/server-docs/im-v1/message/create
     */
    @Data
    public static class Message extends MessageBase {
        private String appId;
        private String appSecret;
    }
}
