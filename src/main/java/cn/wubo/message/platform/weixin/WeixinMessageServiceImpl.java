package cn.wubo.message.platform.weixin;

import cn.wubo.message.core.WeixinPrpperties;
import cn.wubo.message.message.ContentParams;
import cn.wubo.message.message.MarkdownContent;
import cn.wubo.message.message.TextContent;
import cn.wubo.message.platform.AbstractSendService;
import cn.wubo.message.record.IMessageRecordService;
import cn.wubo.message.util.ContentUtils;
import cn.wubo.message.util.WeixinUtils;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import java.util.Map;

@Slf4j
public class WeixinMessageServiceImpl extends AbstractSendService<WeixinPrpperties.Message> {

    private static final String TOUSER = "touser";
    private static final String TOPARTY = "toparty";
    private static final String TOTAG = "totag";

    public WeixinMessageServiceImpl(IMessageRecordService messageRecordService) {
        super(messageRecordService);
    }

    /**
     * 发送Markdown格式的消息。
     *
     * @param aliasProperties 包含企业ID、企业密钥和别名配置的对象，用于认证和指定消息发送的主体。
     * @param content Markdown内容及其发送参数的对象。
     * @return 返回发送消息后的响应结果。
     */
    @Override
    public String sendMarkdown(WeixinPrpperties.Message aliasProperties, MarkdownContent content) {
        // 确保企业ID和企业密钥不为空
        Assert.notNull(aliasProperties.getCorpid(), "corpid should not be null!");
        Assert.notNull(aliasProperties.getCorpsecret(), "corpsecret should not be null!");

        // 根据别名获取消息参数
        Map<String, Object> params = content.getContentParams(aliasProperties.getAlias()).getWeixinMessage();

        // @formatter:off
        // 构建并发送Markdown消息
        return WeixinUtils.message(
                aliasProperties.getCorpid(),
                aliasProperties.getCorpsecret(),
                WeixinUtils.MessageRobotRequest
                        .builder("markdown", aliasProperties.getAgentid())
                        .withMarkdown(ContentUtils.toMarkdown(content))
                        .withToUser((String) params.getOrDefault(TOUSER,null))
                        .withToParty((String) params.getOrDefault(TOPARTY,null))
                        .withToTag((String) params.getOrDefault(TOTAG,null))
                        .build()
        );
        // @formatter:on
    }

    /**
     * 发送文本消息给指定的微信企业号用户、部门或标签。
     *
     * @param aliasProperties 微信属性，包括企业ID、企业密钥和代理ID等信息。
     * @param content 文本消息内容，包括消息文本和别名等信息。
     * @return 返回消息发送结果的字符串表示。
     */
    @Override
    public String sendText(WeixinPrpperties.Message aliasProperties, TextContent content) {
        // 确保企业ID和企业密钥不为空
        Assert.notNull(aliasProperties.getCorpid(), "corpid should not be null!");
        Assert.notNull(aliasProperties.getCorpsecret(), "corpsecret should not be null!");

        // 根据别名获取消息参数
        Map<String, Object> params = content.getContentParams(aliasProperties.getAlias()).getWeixinMessage();

        // @formatter:off
        // 构建并发送Markdown消息
        return WeixinUtils.message(
                aliasProperties.getCorpid(),
                aliasProperties.getCorpsecret(),
                WeixinUtils.MessageRobotRequest
                        .builder("text", aliasProperties.getAgentid())
                        .withText(content.getText())
                        .withToUser((String) params.getOrDefault(TOUSER,null))
                        .withToParty((String) params.getOrDefault(TOPARTY,null))
                        .withToTag((String) params.getOrDefault(TOTAG,null))
                        .build()
        );
        // @formatter:on
    }

}
