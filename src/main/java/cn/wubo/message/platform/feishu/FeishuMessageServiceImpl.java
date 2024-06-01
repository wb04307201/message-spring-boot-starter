package cn.wubo.message.platform.feishu;

import cn.wubo.message.core.FeishuProperties;
import cn.wubo.message.exception.FeishuRuntimeException;
import cn.wubo.message.message.MarkdownContent;
import cn.wubo.message.message.TextContent;
import cn.wubo.message.platform.AbstractSendService;
import cn.wubo.message.util.ContentUtils;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.lark.oapi.Client;
import com.lark.oapi.core.request.RequestOptions;
import com.lark.oapi.core.utils.Lists;
import com.lark.oapi.service.im.v1.model.CreateMessageReq;
import com.lark.oapi.service.im.v1.model.CreateMessageReqBody;
import com.lark.oapi.service.im.v1.model.CreateMessageResp;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FeishuMessageServiceImpl extends AbstractSendService<FeishuProperties.Message> {

    private static final String RECEIVE_ID_TYPE = "receive_id_type";

    /**
     * 发送Markdown消息到飞书
     *
     * @param aliasProperties 飞书消息别名属性，用于配置消息发送的相关属性
     * @param content         Markdown内容对象，包含消息标题和内容
     * @return 返回发送消息后的响应结果，格式为JSON字符串
     */
    @Override
    public String sendMarkdown(FeishuProperties.Message aliasProperties, MarkdownContent content) {
        Assert.notNull(aliasProperties.getAppId(), "appId should not be null!");
        Assert.notNull(aliasProperties.getAppSecret(), "appSecret should not be null!");

        // 构建消息体，包含标题和内容
        JSONObject jo = new JSONObject();
        JSONObject post = new JSONObject();
        post.put("title", content.getTitle());
        post.put("content", new JSONArray(ContentUtils.toPost(content)));
        jo.put("zh_cn", post);

        // 创建飞书客户端
        Client client = Client.newBuilder(aliasProperties.getAppId(), aliasProperties.getAppSecret()).build();

        // 设置请求头，包含消息接收者类型
        Map<String, List<String>> headers = new HashMap<>();
        headers.put(RECEIVE_ID_TYPE, Lists.newArrayList((String) content.getParams().getFeishuMessage().get(RECEIVE_ID_TYPE)));

        try {
            // @formatter:off
            // 发送消息，并获取响应
            CreateMessageResp resp = client.im()
                    .message()
                    .create(
                            CreateMessageReq.newBuilder()
                                    .createMessageReqBody(
                                            CreateMessageReqBody.newBuilder()
                                                    .receiveId((String) content.getParams().getFeishuMessage().get("receive_id"))
                                                    .msgType("post")
                                                    .content(jo.toJSONString())
                                                    .build()
                                    ).build(),
                            RequestOptions.newBuilder()
                                    .headers(headers)
                                    .build()
                    );
            // @formatter:on
            // 将响应结果转换为JSON字符串并返回
            return JSON.toJSONString(resp);
        } catch (Exception e) {
            // 抛出飞书操作运行时异常
            throw new FeishuRuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 发送文本消息到飞书
     *
     * @param aliasProperties 飞书消息别名配置，包含必要的发送信息
     * @param content         消息内容，包括文本和额外参数
     * @return 发送结果，成功返回JSON格式的响应信息，失败返回错误信息字符串
     */
    @Override
    public String sendText(FeishuProperties.Message aliasProperties, TextContent content) {
        Assert.notNull(aliasProperties.getAppId(), "appId should not be null!");
        Assert.notNull(aliasProperties.getAppSecret(), "appSecret should not be null!");

        // 构造消息体，包含文本内容
        JSONObject jo = new JSONObject();
        jo.put("text", content.getText());

        // 创建飞书客户端
        Client client = Client.newBuilder(aliasProperties.getAppId(), aliasProperties.getAppSecret()).build();

        // 设置请求头，包含消息接收者类型
        Map<String, List<String>> headers = new HashMap<>();
        headers.put(RECEIVE_ID_TYPE, Lists.newArrayList((String) content.getContentParams(aliasProperties.getAlias()).getFeishuMessage().get(RECEIVE_ID_TYPE)));

        try {
            // @formatter:off
            // 创建并发送消息，获取响应
            CreateMessageResp resp = client.im()
                    .message()
                    .create(
                            CreateMessageReq.newBuilder()
                                    .createMessageReqBody(
                                            CreateMessageReqBody.newBuilder()
                                                    .receiveId((String) content.getParams().getFeishuMessage().get("receive_id"))
                                                    .msgType("text")
                                                    .content(jo.toJSONString())
                                                    .build()
                                    )
                                    .build(),
                            RequestOptions.newBuilder()
                                    .headers(headers)
                                    .build()
                    );
            // @formatter:on
            // 将响应转为JSON字符串返回
            return JSON.toJSONString(resp);
        } catch (Exception e) {
            // 记录错误信息，并返回错误信息字符串
            throw new FeishuRuntimeException(e.getMessage(), e);
        }
    }
}
