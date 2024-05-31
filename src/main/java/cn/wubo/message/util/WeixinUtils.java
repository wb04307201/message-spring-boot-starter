package cn.wubo.message.util;

import com.alibaba.fastjson2.JSONObject;

public class WeixinUtils {

    private WeixinUtils() {
    }

    private static final String WEBHOOK = "https://qyapi.weixin.qq.com/cgi-bin/webhook/send?key=%s";
    private static final String GET_TOKEN = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=%s&corpsecret=%s";
    private static final String MESSAGE = "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=%s";

    /**
     * 向自定义机器人发送请求。
     *
     * @param key  机器人的密钥，用于构建请求的URL。
     * @param body 请求体的内容，通常为JSON格式，具体格式依赖于机器人的要求。
     * @return 从机器人服务接收到的响应内容，通常为字符串格式。
     */
    public static String requestCustomRobot(String key, String body) {
        // @formatter:off
        // 构建REST客户端请求，设置URI、请求头、请求体，并发送POST请求，然后获取响应体
        return RestClientUtils.getRestClient()
                .post()
                .uri(String.format(WEBHOOK, key)) // 使用密钥格式化 webhook URL
                .headers(ApiUtils.getJsonContentHeaders()) // 设置请求头，指定内容类型为JSON
                .body(body) // 设置请求体
                .retrieve() // 发送请求并获取响应
                .toEntity(String.class) // 将响应内容解析为字符串类型
                .getBody(); // 获取响应体内容
        // @formatter:on
    }

    /**
     * 获取企业微信访问令牌。
     * 该令牌用于企业微信API的授权验证。
     *
     * @param corpid 企业标识，即企业ID，在企业微信管理后台可获取。
     * @param corpsecret 企业密钥，用于验证企业身份，同样在企业微信管理后台可获取。
     * @return 返回一个包含访问令牌的JSONObject。其中，访问令牌存储在键为"access_token"的字段中。
     */
    public static JSONObject getToken(String corpid, String corpsecret) {
        // @formatter:off
        // 使用RestClientUtils的getRestClient方法获取RestClient实例
        // 然后构造GET请求，指定获取令牌的URL，并设置请求头为JSON内容类型
        // 发送请求并检索响应体，将其转换为JSONObject类型并返回
        return RestClientUtils.getRestClient()
                .get()
                .uri(String.format(GET_TOKEN, corpid, corpsecret)) // 使用corpid和corpsecret格式化URL
                .headers(ApiUtils.getJsonContentHeaders()) // 设置请求头
                .retrieve() // 发送请求并获取响应
                .toEntity(JSONObject.class) // 将响应体转换为JSONObject类型
                .getBody(); // 获取转换后的响应体实体
        // @formatter:on
    }

    /**
     * 使用访问令牌向消息服务发送请求。
     *
     * @param accessToken 访问资源的令牌，用于验证和授权。
     * @param body        发送给消息服务的消息体，内容和格式需符合API要求，通常为JSON格式。
     * @return 服务响应的消息内容，以字符串形式返回。
     */
    public static String message(String accessToken, String body) {
        // @formatter:off
        // 创建POST请求，配置URI、请求头、请求体，执行请求并处理响应
        return RestClientUtils.getRestClient()
                .post()
                .uri(String.format(MESSAGE, accessToken)) // 使用访问令牌构造请求URL
                .headers(ApiUtils.getJsonContentHeaders()) // 设置请求头，适应JSON数据传输
                .body(body) // 设置请求的具体内容
                .retrieve() // 发起HTTP POST请求
                .toEntity(String.class) // 指定响应体应被转换为String类型
                .getBody(); // 获取实际的响应消息体
        // @formatter:on
    }

}
