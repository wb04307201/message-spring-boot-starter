package cn.wubo.message.platform.feishu;

import cn.wubo.message.core.FeishuProperties;
import cn.wubo.message.core.MessageType;
import cn.wubo.message.message.MarkdownContent;
import cn.wubo.message.message.TextContent;
import cn.wubo.message.platform.AbstractSendService;
import cn.wubo.message.record.IMessageRecordService;
import cn.wubo.message.util.ContentUtils;
import cn.wubo.message.util.FeishuUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import java.util.stream.Collectors;


@Slf4j
public class FeishuCustomRobotServiceImpl extends AbstractSendService<FeishuProperties.CustomRobot> {

    public FeishuCustomRobotServiceImpl(IMessageRecordService messageRecordService) {
        super(messageRecordService);
    }

    @Override
    public String sendMarkdown(FeishuProperties.CustomRobot aliasProperties, MarkdownContent content) {
        JSONObject jo = new JSONObject();
        jo.put("msgtype", "post");
        JSONArray ja = ContentUtils.toPost(content);
        content.getContentParams(aliasProperties.getAlias()).getFeishuCustomRobot().entrySet().stream().forEach(entry -> {
            JSONObject temp = new JSONObject();
            temp.put("tag", "at");
            temp.put("user_id", entry.getKey());
            temp.put("user_name", entry.getValue());
            ja.add(temp);
        });
        JSONObject post = new JSONObject();
        post.put("title", content.getTitle());
        post.put("content", new JSONArray(ja));
        jo.put("content", new JSONObject().put("post", new JSONObject().put("zh_cn", post)));
        try {
            return request(aliasProperties, jo);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            log.error(e.getMessage(), e);
            return e.getMessage();
        }
    }

    @Override
    public String sendText(FeishuProperties.CustomRobot aliasProperties, TextContent content) {
        JSONObject jo = new JSONObject();
        jo.put("msgtype", "text");
        jo.put("content", content.getContentParams(aliasProperties.getAlias()).getFeishuCustomRobot().entrySet().stream().map(entry -> "<at user_id = \"" + entry.getKey() + "\">" + entry.getValue() + "</at>").collect(Collectors.joining(" ")) + " " + content.getText());
        try {
            return request(aliasProperties, jo);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            log.error(e.getMessage(), e);
            return e.getMessage();
        }
    }

    private String request(FeishuProperties.CustomRobot aliasProperties, JSONObject jo) throws NoSuchAlgorithmException, InvalidKeyException {
        Long timestamp = System.currentTimeMillis();
        jo.put("timestamp", timestamp);
        jo.put("sign", FeishuUtils.sign(timestamp, aliasProperties.getSecret()));
        return FeishuUtils.requestCustomRobot(Objects.requireNonNull(aliasProperties.getHookid()), jo.toJSONString());
    }
}
