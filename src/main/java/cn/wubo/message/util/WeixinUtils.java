package cn.wubo.message.util;

import com.alibaba.fastjson.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

public class WeixinUtils {

    private final static String WEBHOOK = "https://qyapi.weixin.qq.com/cgi-bin/webhook/send?key=%s";
    private final static String GET_TOKEN = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=%s&corpsecret=%s";
    private final static String MESSAGE = "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=%s";

    public static String requestCustomRobot(String key, String body) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(HttpHeaders.CONTENT_TYPE, "application/json; charset=utf-8");
        HttpEntity<String> request = new HttpEntity<>(body, httpHeaders);
        return restTemplate.postForObject(String.format(WEBHOOK, key), request, String.class);
    }

    public static JSONObject getToken(String corpid, String corpsecret) {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(URI.create(String.format(GET_TOKEN, corpid, corpsecret)), JSONObject.class);
    }

    public static String message(String accessToken, String body) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(HttpHeaders.CONTENT_TYPE, "application/json; charset=utf-8");
        HttpEntity<String> request = new HttpEntity<>(body, httpHeaders);
        return restTemplate.postForObject(String.format(MESSAGE, accessToken), request, String.class);
    }
}
