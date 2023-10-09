package cn.wubo.message.util;

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


    public static DingTalkClient getCustomRobotClient(String accessToken, String secret) throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException {
        Long timestamp = System.currentTimeMillis();
        String stringToSign = timestamp + "\n" + secret;
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
        byte[] signData = mac.doFinal(stringToSign.getBytes(StandardCharsets.UTF_8));
        String sign = URLEncoder.encode(new String(Base64.encodeBase64(signData)), "UTF-8");
        return new DefaultDingTalkClient(String.format(WEBHOOK, accessToken, timestamp, sign));
    }

    public static OapiGettokenResponse getToken(String appkey, String appsecret) throws ApiException {
        DingTalkClient client = new DefaultDingTalkClient(GET_TOKEN);
        OapiGettokenRequest request = new OapiGettokenRequest();
        request.setAppkey(appkey);
        request.setAppsecret(appsecret);
        request.setHttpMethod("GET");
        return client.execute(request);
    }

    public static DingTalkClient getMessageClient() {
        return new DefaultDingTalkClient(MESSAGE);
    }
}
