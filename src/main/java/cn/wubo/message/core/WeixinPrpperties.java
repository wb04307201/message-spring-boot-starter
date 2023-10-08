package cn.wubo.message.core;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class WeixinPrpperties {

    private List<DingtalkProperties.CustomRobot> customRobot = new ArrayList<>();

    private List<DingtalkProperties.Message> message = new ArrayList<>();

    /**
     * 自定义机器人
     * https://developer.work.weixin.qq.com/document/path/99110
     */
    @Data
    public static class CustomRobot extends MessageBase {
        private String key;
    }

    /**
     * 发送应用消息
     * https://developer.work.weixin.qq.com/document/path/90236
     */
    @Data
    public static class Message extends MessageBase {
        private String corpid;
        private String corpsecret;
        private String agentid;

    }
}
