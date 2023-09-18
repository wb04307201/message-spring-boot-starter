package cn.wubo.message.core;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class WeixinPrpperties {

    private List<DingtalkProperties.CustomRobot> customRobot = new ArrayList<>();

    /**
     * 自定义机器人
     * https://developer.work.weixin.qq.com/document/path/99110
     */
    @Data
    @Builder
    public static class CustomRobot extends MessageBase {
        private String key;
    }

    /**
     * 发送应用消息
     * https://developer.work.weixin.qq.com/document/path/90236
     */
    @Data
    @Builder
    public static class Message extends MessageBase {
        private String corpid;
        private String corpsecret;
        private String agentid;

    }
}
