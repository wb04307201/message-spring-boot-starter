package cn.wubo.message.platform.wx;

import cn.wubo.message.core.WeixinPrpperties;
import cn.wubo.message.message.MarkdownContent;
import cn.wubo.message.message.TextContent;
import cn.wubo.message.platform.AbstractSendService;
import cn.wubo.message.record.IMessageRecordService;
import cn.wubo.message.util.ContentUtils;
import cn.wubo.message.util.WeixinUtils;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class WeixinCustomRobotServiceImpl extends AbstractSendService<WeixinPrpperties.CustomRobot> {

    protected WeixinCustomRobotServiceImpl(IMessageRecordService messageRecordService) {
        super(messageRecordService);
    }

    @Override
    public String sendMarkdown(WeixinPrpperties.CustomRobot aliasProperties, MarkdownContent content) {
        JSONObject jo = new JSONObject();
        jo.put("msgtype", "markdown");
        jo.put("markdown", new JSONObject().put("content", ContentUtils.toMarkdown(content)));
        return WeixinUtils.requestCustomRobot(aliasProperties.getKey(), jo.toJSONString());
    }

    @Override
    public String sendText(WeixinPrpperties.CustomRobot aliasProperties, TextContent content) {
        JSONObject jo = new JSONObject();
        jo.put("msgtype", "text");
        jo.put("text", new JSONObject().put("content", content.getText()));
        return WeixinUtils.requestCustomRobot(aliasProperties.getKey(), jo.toJSONString());
    }
}
