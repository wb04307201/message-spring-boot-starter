package cn.wubo.message.platform.feishu;

import cn.wubo.message.core.FeishuProperties;
import cn.wubo.message.core.MessageType;
import cn.wubo.message.message.MarkdownContent;
import cn.wubo.message.message.TextContent;
import cn.wubo.message.platform.AbstractSendService;
import cn.wubo.message.record.IMessageRecordService;
import cn.wubo.message.util.ContentUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lark.oapi.Client;
import com.lark.oapi.core.request.RequestOptions;
import com.lark.oapi.core.utils.Lists;
import com.lark.oapi.service.im.v1.model.CreateMessageReq;
import com.lark.oapi.service.im.v1.model.CreateMessageReqBody;
import com.lark.oapi.service.im.v1.model.CreateMessageResp;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
public class FeishuMessageServiceImpl extends AbstractSendService<FeishuProperties.Message> {

    protected FeishuMessageServiceImpl(IMessageRecordService messageRecordService) {
        super(messageRecordService);
    }

    @Override
    public String sendMarkdown(FeishuProperties.Message aliasProperties, MarkdownContent content) {
        JSONObject jo = new JSONObject();
        JSONObject post = new JSONObject();
        post.put("title", content.getTitle());
        post.put("content", new JSONArray(ContentUtils.toPost(content)));
        jo.put("zh_cn", post);
        Client client = Client.newBuilder("appId", "appSecret").build();
        Map<String, List<String>> headers = new HashMap<>();
        headers.put("receive_id_type", Lists.newArrayList((String) content.getParams().getFeishuMessage().get("receive_id_type")));
        try {
            CreateMessageResp resp = client.im().message().create(CreateMessageReq.newBuilder().createMessageReqBody(CreateMessageReqBody.newBuilder().receiveId((String) content.getParams().getFeishuMessage().get("receive_id")).msgType("post").content(jo.toJSONString()).build()).build(), RequestOptions.newBuilder().headers(headers).build());
            return JSON.toJSONString(resp);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return e.getMessage();
        }
    }

    @Override
    public String sendText(FeishuProperties.Message aliasProperties, TextContent content) {
        JSONObject jo = new JSONObject();
        jo.put("text", content.getText());
        Client client = Client.newBuilder("appId", "appSecret").build();
        Map<String, List<String>> headers = new HashMap<>();
        headers.put("receive_id_type", Lists.newArrayList((String) content.getContentParams(aliasProperties.getAlias(), MessageType.getMessageType(aliasProperties)).getFeishuMessage().get("receive_id_type")));
        try {
            CreateMessageResp resp = client.im().message().create(CreateMessageReq.newBuilder().createMessageReqBody(CreateMessageReqBody.newBuilder().receiveId((String) content.getParams().getFeishuMessage().get("receive_id")).msgType("text").content(jo.toJSONString()).build()).build(), RequestOptions.newBuilder().headers(headers).build());
            return JSON.toJSONString(resp);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return e.getMessage();
        }
    }
}
