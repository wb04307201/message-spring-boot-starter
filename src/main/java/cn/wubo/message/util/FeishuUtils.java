package cn.wubo.message.util;

import cn.wubo.message.exception.FeishuRuntimeException;
import org.apache.commons.codec.binary.Base64;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class FeishuUtils {

    private final static String WEBHOOK = "https://open.feishu.cn/open-apis/bot/v2/hook/%s";

    public static String requestCustomRobot(String hookid, String body) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(HttpHeaders.CONTENT_TYPE, "application/json; charset=utf-8");
        HttpEntity<String> request = new HttpEntity<>(body, httpHeaders);
        return restTemplate.postForObject(String.format(WEBHOOK, hookid), request, String.class);
    }

    public static String sign(Long timestamp, String secret) throws NoSuchAlgorithmException, InvalidKeyException {
        String stringToSign = timestamp + "\n" + secret;
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(stringToSign.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
        byte[] signData = mac.doFinal(new byte[]{});
        return new String(Base64.encodeBase64(signData));
    }
}
