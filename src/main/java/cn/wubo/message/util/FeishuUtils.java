package cn.wubo.message.util;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class FeishuUtils {

    private FeishuUtils() {
    }

    private static final String WEBHOOK = "https://open.feishu.cn/open-apis/bot/v2/hook/%s";

    /**
     * 向指定的机器人Webhook发送自定义消息。
     *
     * @param hookid Webhook的ID，用于指定接收消息的机器人。
     * @param body   要发送的消息内容，需要以JSON或其他支持的格式提供。
     * @return 返回从机器人服务接收到的响应内容。
     */
    public static String requestCustomRobot(String hookid, String body) {
        // @formatter:off
        // 使用RestClient向指定Webhook发送POST请求并获取响应体
        return RestClientUtils.getRestClient()
                .post()
                .uri(String.format(WEBHOOK, hookid)) // 格式化URL，插入hookid
                .headers(ApiUtils.getJsonContentHeaders()) // 设置请求头，指定内容类型为JSON
                .body(body) // 设置请求体，即要发送的消息
                .retrieve() // 发送请求并获取响应
                .toEntity(String.class) // 将响应内容转换为String类型
                .getBody(); // 获取响应体内容
        // @formatter:on
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
    public static String sign(Long timestamp, String secret) throws NoSuchAlgorithmException, InvalidKeyException {
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
