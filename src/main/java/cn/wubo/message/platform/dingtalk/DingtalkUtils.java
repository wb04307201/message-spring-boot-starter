package cn.wubo.message.platform.dingtalk;

import cn.wubo.message.util.CaffieneCache;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiGettokenRequest;
import com.dingtalk.api.response.OapiGettokenResponse;
import com.taobao.api.ApiException;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class DingtalkUtils {

    private DingtalkUtils() {
    }

    private static final String WEBHOOK = "https://oapi.dingtalk.com/robot/send?access_token=%s&timestamp=%s&sign=%s";
    private static final String GET_TOKEN = "https://oapi.dingtalk.com/gettoken";
    private static final String MESSAGE = "https://oapi.dingtalk.com/topapi/message/corpconversation/asyncsend_v2?access_token=%s";

    /**
     * 根据访问令牌(accessToken)和密钥(secret)获取钉钉自定义机器人客户端。
     * 该方法用于构建一个带有签名的钉钉自定义机器人消息发送客户端。
     * 签名机制用于确保请求的安全性。
     *
     * @param accessToken 钉钉自定义机器人访问令牌，用于标识发送消息的权限。
     * @param secret 钉钉自定义机器人的密钥，用于生成签名。
     * @return 返回一个配置了访问令牌和签名的DingTalkClient实例。
     * @throws NoSuchAlgorithmException 如果HmacSHA256算法不可用，则抛出此异常。
     * @throws InvalidKeyException 如果密钥无效，则抛出此异常。
     */
    public static DingTalkClient getCustomRobotClient(String accessToken, String secret) throws NoSuchAlgorithmException, InvalidKeyException {
        // 获取当前时间戳，用于签名的时效性验证
        Long timestamp = System.currentTimeMillis();
        // 构建待签名字符串，包括时间戳和密钥
        String stringToSign = timestamp + "\n" + secret;
        // 初始化HmacSHA256加密实例
        Mac mac = Mac.getInstance("HmacSHA256");
        // 使用密钥初始化加密实例
        mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
        // 对待签名字符串进行加密生成签名数据
        byte[] signData = mac.doFinal(stringToSign.getBytes(StandardCharsets.UTF_8));
        // 对签名数据进行Base64编码，并使用UTF-8编码进行URL编码，以符合钉钉接口要求
        String sign = URLEncoder.encode(new String(Base64.encodeBase64(signData)), StandardCharsets.UTF_8);
        // 构建带有访问令牌、时间戳和签名的钉钉自定义机器人消息发送地址，并返回DingTalkClient实例
        return new DefaultDingTalkClient(String.format(WEBHOOK, accessToken, timestamp, sign));
    }


    /**
     * 获取钉钉应用的访问令牌。
     *
     * 本方法首先尝试从缓存中获取令牌。如果缓存中不存在，则通过调用钉钉接口来获取令牌，
     * 并将获取到的令牌及其过期时间存入缓存中。
     *
     * @param appkey 钉钉应用的appkey，用于标识应用。
     * @param appsecret 钉钉应用的appsecret，用于验证应用的身份。
     * @return 返回钉钉应用的访问令牌。
     * @throws ApiException 如果获取令牌过程中发生错误，则抛出此异常。
     */
    public static String getToken(String appkey, String appsecret) throws ApiException {
        // 尝试从缓存中获取令牌
        String token = CaffieneCache.getToken("dingTalk-" + appkey);
        if (token == null) {
            // 缓存中未找到令牌，需要通过调用接口来获取
            DingTalkClient client = new DefaultDingTalkClient(GET_TOKEN);
            OapiGettokenRequest request = new OapiGettokenRequest();
            request.setAppkey(appkey);
            request.setAppsecret(appsecret);
            request.setHttpMethod("GET");
            // 执行接口调用，获取令牌
            OapiGettokenResponse oapiGettokenResponse = client.execute(request);
            // 将获取到的令牌及其过期时间存入缓存
            token = CaffieneCache.setToken("dingTalk-" + appkey, oapiGettokenResponse.getExpiresIn(), oapiGettokenResponse.getAccessToken());
        }
        // 返回令牌
        return token;
    }

    /**
     * 获取用于发送消息的钉钉客户端。
     *
     * 该方法通过创建一个默认的钉钉客户端实例来实现。这个客户端专门用于发送各种类型的消息到钉钉平台。
     * 它是整个应用程序与钉钉消息接口的桥梁，封装了与钉钉API的交互逻辑。
     *
     * @return DefaultDingTalkClient 实例，用于发送消息。
     */
    public static DingTalkClient getMessageClient() {
        // 使用预定义的钉钉消息URL创建一个新的DefaultDingTalkClient实例
        return new DefaultDingTalkClient(MESSAGE);
    }
}
