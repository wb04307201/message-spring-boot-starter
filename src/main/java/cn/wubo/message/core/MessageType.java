package cn.wubo.message.core;

import cn.wubo.message.platform.ISendService;
import cn.wubo.message.platform.dingtalk.DingtalkCustomRobotServiceImpl;
import cn.wubo.message.platform.dingtalk.DingtalkMessageServiceImpl;
import cn.wubo.message.platform.feishu.FeishuCustomRobotServiceImpl;
import cn.wubo.message.platform.feishu.FeishuMessageServiceImpl;
import cn.wubo.message.platform.mail.MailSmtpServiceImpl;
import cn.wubo.message.platform.wx.WeixinCustomRobotServiceImpl;
import cn.wubo.message.platform.wx.WeixinMessageServiceImpl;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public enum MessageType {

    DingtalkCustomRobot("cn.wubo.message.core.DingtalkProperties$CustomRobot"), DingtalkMessage("cn.wubo.message.core.DingtalkProperties$Message"), FeishuCustomRobot("cn.wubo.message.core.FeishuProperties$CustomRobot"), FeishuMessage("cn.wubo.message.core.FeishuProperties$Message"), WeixinCustomRobot("cn.wubo.message.core.WeixinPrpperties$CustomRobot"), WeixiMessage("cn.wubo.message.core.WeixinPrpperties$Message"), MailSmtp("cn.wubo.message.core.MailProperties$Smtp"),
    ;

    @Getter
    private final String className;

    MessageType(String className) {
        this.className = className;
    }

    public static MessageType getMessageType(MessageBase messageBase) {
        return MessageType.valueOf(messageBase.getClass().getName());
    }

    private static final Map<MessageType, Class<? extends ISendService>> MESSAGE_TYPE_MAPPER = new HashMap<>();

    static {
        MESSAGE_TYPE_MAPPER.put(MessageType.DingtalkCustomRobot, DingtalkCustomRobotServiceImpl.class);
        MESSAGE_TYPE_MAPPER.put(MessageType.DingtalkMessage, DingtalkMessageServiceImpl.class);
        MESSAGE_TYPE_MAPPER.put(MessageType.FeishuCustomRobot, FeishuCustomRobotServiceImpl.class);
        MESSAGE_TYPE_MAPPER.put(MessageType.FeishuMessage, FeishuMessageServiceImpl.class);
        MESSAGE_TYPE_MAPPER.put(MessageType.WeixinCustomRobot, WeixinCustomRobotServiceImpl.class);
        MESSAGE_TYPE_MAPPER.put(MessageType.WeixiMessage, WeixinMessageServiceImpl.class);
        MESSAGE_TYPE_MAPPER.put(MessageType.MailSmtp, MailSmtpServiceImpl.class);
    }

    public static Class<? extends ISendService> getClass(MessageBase messageBase) {
        return MESSAGE_TYPE_MAPPER.get(getMessageType(messageBase));
    }
}
