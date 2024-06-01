package cn.wubo.message.platform.feishu;

import cn.wubo.message.core.FeishuProperties;
import cn.wubo.message.exception.FeishuRuntimeException;
import cn.wubo.message.message.MarkdownContent;
import cn.wubo.message.message.TextContent;
import cn.wubo.message.platform.AbstractSendService;
import cn.wubo.message.record.IMessageRecordService;
import cn.wubo.message.util.ContentUtils;
import cn.wubo.message.util.FeishuUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.stream.Collectors;

public class FeishuCustomRobotServiceImpl extends AbstractSendService<FeishuProperties.CustomRobot> {

    public FeishuCustomRobotServiceImpl(IMessageRecordService messageRecordService) {
        super(messageRecordService);
    }

    /**
     * 发送Markdown消息到飞书
     *
     * @param aliasProperties 飞书机器人别名配置属性，包含机器人的hookid和别名等信息
     * @param content         Markdown内容对象，包含消息的具体内容
     * @return 发送消息后的响应结果，通常为一个JSON字符串
     */
    @Override
    public String sendMarkdown(FeishuProperties.CustomRobot aliasProperties, MarkdownContent content) {
        // 将Markdown内容转换为飞书机器人可接受的格式列表
        List<FeishuUtils.CustomRobotRequest.PostContenDetail> list = ContentUtils.toPost(content);

        // @formatter:off
        // 为消息添加@别名的功能
        content.getContentParams(aliasProperties.getAlias()).getFeishuCustomRobot()
                .entrySet()
                .stream()
                .forEach(
                        entry -> list.add(new FeishuUtils.CustomRobotRequest.PostContenDetail("at", null, null, entry.getKey(), (String) entry.getValue()))
                );
        // @formatter:on

        // 获取当前时间戳，用于消息签名
        Long timestamp = System.currentTimeMillis();
        try {
            // @formatter:off
            // 构建并发送消息到飞书机器人
            return FeishuUtils.customRobot(
                    aliasProperties.getHookid(),
                    FeishuUtils.CustomRobotRequest.builder("post", timestamp, sign(timestamp, aliasProperties.getSecret()))
                            .withPost(content.getTitle(), list)
                            .build()
            );
            // @formatter:on
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            // 处理签名算法异常，抛出运行时异常
            return e.getMessage();
        }
    }


    /**
     * 发送文本消息到飞书机器人。
     *
     * @param aliasProperties 飞书自定义机器人的配置属性，包括机器人hook ID和密钥等。
     * @param content         消息的内容，支持文本内容的定制化。
     * @return 返回发送消息后的响应结果。
     * @throws FeishuRuntimeException 如果在签名算法处理过程中发生异常，则抛出此运行时异常。
     */
    @Override
    public String sendText(FeishuProperties.CustomRobot aliasProperties, TextContent content) {
        Long timestamp = System.currentTimeMillis();
        try {
            // @formatter:off
            // 构建并发送自定义机器人消息请求，其中包含消息的文本内容。
            return FeishuUtils.customRobot(
                    aliasProperties.getHookid(),
                    FeishuUtils.CustomRobotRequest.builder("text", timestamp, sign(timestamp, aliasProperties.getSecret()))
                            .withText(
                                    new FeishuUtils.CustomRobotRequest.TextContent(
                                            content.getContentParams(aliasProperties.getAlias()).getFeishuCustomRobot()
                                                    .entrySet()
                                                    .stream()
                                                    .map(entry -> "<at user_id = \"" + entry.getKey() + "\">" + entry.getValue() + "</at>").
                                                    collect(Collectors.joining(" "))
                                                    + " "
                                                    + content.getText()
                                    )
                            )
                            .build()
            );
            // @formatter:on
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            // 处理签名算法异常，将异常信息封装并抛出为运行时异常。
            throw new FeishuRuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 使用HmacSHA256算法对给定的时间戳和密钥进行签名。
     *
     * @param timestamp 需要签名的时间戳，单位为毫秒。
     * @param secret    用于签名的密钥。
     * @return 返回经过Base64编码的签名数据字符串。
     * @throws NoSuchAlgorithmException 当指定的算法不可用时抛出该异常。
     * @throws InvalidKeyException      当密钥无效时抛出该异常。
     */
    private String sign(Long timestamp, String secret) throws NoSuchAlgorithmException, InvalidKeyException {
        // 构建待签名字符串，将时间戳和密钥以换行符分隔
        String stringToSign = timestamp + "\n" + secret;

        // 初始化HmacSHA256消息认证码对象
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(stringToSign.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));

        // 计算签名
        byte[] signData = mac.doFinal(new byte[]{});

        // 将签名数据使用Base64编码成字符串后返回
        return new String(Base64.encodeBase64(signData));
    }
}
