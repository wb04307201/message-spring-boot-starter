package cn.wubo.message.message;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class ContentParams {
    private Map<String, Object> dingtalkCustomRobot = new HashMap<>();
    private Map<String, Object> dingtalkMessage = new HashMap<>();
    private Map<String, Object> feishuCustomRobot = new HashMap<>();
    private Map<String, Object> feishuMessage = new HashMap<>();
    private Map<String, Object> weixinMessage = new HashMap<>();
    private Map<String, Object> mailSmtp = new HashMap<>();
}
