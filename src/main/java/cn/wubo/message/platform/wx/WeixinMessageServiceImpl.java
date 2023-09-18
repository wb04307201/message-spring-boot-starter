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
import java.util.Objects;

@Slf4j
public class WeixinMessageServiceImpl extends AbstractSendService<WeixinPrpperties.Message> {

    protected WeixinMessageServiceImpl(IMessageRecordService messageRecordService) {
        super(messageRecordService);
    }

    @Override
    public String sendMarkdown(WeixinPrpperties.Message aliasProperties, MarkdownContent content) {
        JSONObject result = WeixinUtils.getToken(Objects.requireNonNull(aliasProperties.getCorpid()), Objects.requireNonNull(aliasProperties.getCorpsecret()));
        JSONObject jo = request(aliasProperties, content.getContentParams(aliasProperties.getAlias()).getWeixinMessage());
        jo.put("msgtype", "markdown");
        jo.put("markdown", new JSONObject().put("content", ContentUtils.toMarkdown(content, "\n")));
        return WeixinUtils.message(result.getString("access_token"), jo.toJSONString());
    }

    @Override
    public String sendText(WeixinPrpperties.Message aliasProperties, TextContent content) {
        JSONObject result = WeixinUtils.getToken(Objects.requireNonNull(aliasProperties.getCorpid()), Objects.requireNonNull(aliasProperties.getCorpsecret()));
        JSONObject jo = request(aliasProperties, content.getContentParams(aliasProperties.getAlias()).getWeixinMessage());
        jo.put("msgtype", "text");
        jo.put("text", new JSONObject().put("content", content.getText()));
        return WeixinUtils.message(result.getString("access_token"), jo.toJSONString());
    }

    private JSONObject request(WeixinPrpperties.Message aliasProperties, Map<String, Object> params) {
        JSONObject jo = new JSONObject();
        if (params.containsKey("touser")) jo.put("touser", params.get("touser"));
        if (params.containsKey("toparty")) jo.put("toparty", params.get("toparty"));
        if (params.containsKey("totag")) jo.put("totag", params.get("totag"));
        jo.put("agentid", aliasProperties.getAgentid());
        return jo;
    }
}
