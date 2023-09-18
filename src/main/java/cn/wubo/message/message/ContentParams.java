package cn.wubo.message.message;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class ContentParams {
    private Map<String, Object> dingtalkCustomRobot = new HashMap<>();
    private Map<String, Object> dingtalkMessage = new HashMap<>();
    private Map<String, Object> FeishuCustomRobot = new HashMap<>();
    private Map<String, Object> FeishuMessage = new HashMap<>();
    private Map<String, Object> WeixinMessage = new HashMap<>();
    private Map<String, Object> MailSmtp = new HashMap<>();
}
