package cn.wubo.message.config;

import cn.wubo.message.core.DingtalkProperties;
import cn.wubo.message.core.FeishuProperties;
import cn.wubo.message.core.MailProperties;
import cn.wubo.message.core.WeixinPrpperties;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "message")
public class MessageConfigurationProperties {

    private String messageRecord = "cn.wubo.message.record.impl.MemMessageRecordServiceImpl";

    private DingtalkProperties dingtalk = new DingtalkProperties();
    private FeishuProperties feishu = new FeishuProperties();
    private WeixinPrpperties weixin = new WeixinPrpperties();
    private MailProperties mail = new MailProperties();

}
