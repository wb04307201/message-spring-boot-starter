package cn.wubo.message.platform.weixin;

import cn.wubo.message.core.WeixinPrpperties;
import cn.wubo.message.message.MarkdownContent;
import cn.wubo.message.message.TextContent;
import cn.wubo.message.platform.AbstractSendService;
import cn.wubo.message.util.ContentUtils;
import org.springframework.util.Assert;

public class WeixinCustomRobotServiceImpl extends AbstractSendService<WeixinPrpperties.CustomRobot> {

    /**
     * 使用markdown格式发送消息到微信自定义机器人。
     *
     * @param aliasProperties 用于配置微信自定义机器人的别名属性，包含关键的访问令牌。
     * @param content         要发送的markdown内容。
     * @return 返回从微信服务器接收到的响应信息。
     */
    @Override
    public String sendMarkdown(WeixinPrpperties.CustomRobot aliasProperties, MarkdownContent content) {
        Assert.notNull(aliasProperties.getKey(), "key should not be null!");
        // @formatter:off
        // 构建向微信自定义机器人发送的消息请求，其中消息类型设定为markdown，内容为传入的markdown格式文本
        return WeixinUtils.customRobot(
                aliasProperties.getKey(),
                WeixinUtils.CustomRobotRequest
                        .builder("markdown")
                        .withMarkdown(ContentUtils.toMarkdown(content))
                        .build()
        );
        // @formatter:on
    }

    /**
     * 发送文本消息到微信自定义机器人
     *
     * @param aliasProperties 用于配置微信自定义机器人的别名属性，包含关键的访问令牌
     * @param content         要发送的文本内容
     * @return 发送结果的字符串反馈
     */
    @Override
    public String sendText(WeixinPrpperties.CustomRobot aliasProperties, TextContent content) {
        Assert.notNull(aliasProperties.getKey(), "key should not be null!");
        // @formatter:off
        // 构建并发送自定义机器人文本消息
        return WeixinUtils.customRobot(
                aliasProperties.getKey(), // 使用别名属性中的关键令牌
                WeixinUtils.CustomRobotRequest
                        .builder("text") // 创建消息请求构建器,指定消息类型为文本
                        .withText(content.getText()) // 设置文本消息内容
                        .build() // 构建消息请求
        );
        // @formatter:on
    }
}
