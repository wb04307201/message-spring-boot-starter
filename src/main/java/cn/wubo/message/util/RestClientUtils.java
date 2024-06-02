package cn.wubo.message.util;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

import java.util.function.Consumer;

public class RestClientUtils {

    private static RestClient restClient;

    private RestClientUtils() {
    }

    public static synchronized RestClient getRestClient() {
        if (restClient == null) return RestClient.builder().build();
        return restClient;
    }

    /**
     * 获取设置JSON内容类型的HttpHeaders消费者函数。
     * 这个函数不接受任何参数，返回一个Consumer接口的实例，该实例接受HttpHeaders作为参数，
     * 并将HTTP头的媒体类型设置为application/json。
     *
     * @return Consumer<HttpHeaders> 一个消费者函数，用于设置HTTP头的内容类型为JSON。
     */
    public static Consumer<HttpHeaders> getJsonContentHeaders() {
        // 返回一个Lambda表达式，设置HttpHeaders的内容类型为JSON
        return headers -> headers.setContentType(MediaType.APPLICATION_JSON);
    }

    /**
     * 向指定URL发送POST请求，并返回响应体。
     *
     * @param url  请求的URL地址，指定发送POST请求的地址。
     * @param body 请求体的内容，POST请求中携带的数据。
     * @return 响应体的内容，类型为String。发送POST请求后接收到的服务器响应。
     */
    public static String post(String url, Object body) {
        // @formatter:off
        // 使用RestClient发送POST请求，设置请求的URL、请求头和请求体
        return getRestClient().post()
                .uri(url)
                .headers(getJsonContentHeaders())
                .body(body)
                // 发送请求并获取响应体
                .retrieve()
                .toEntity(String.class)
                .getBody();
        // @formatter:on
    }

    /**
     * 向指定URL发出GET请求并返回响应内容。
     *
     * @param url 请求的URL地址
     * @return 返回从指定URL获得的响应内容，类型为String
     */
    public static String get(String url) {
        // 调用重载的get方法，将URL和期望的响应类型String传入
        return get(url, String.class);
    }

    /**
     * 向指定的URL发送GET请求，并返回响应体。
     *
     * @param url         请求的URL地址，指定要发送GET请求的资源位置。
     * @param entityClass 响应体的期望类型，用于将响应体解析为特定类型的对象。
     * @return 响应体的内容，类型为指定的entityClass类型。通过该方法，可以将返回的JSON或XML等数据自动解析为Java对象。
     */
    public static <T> T get(String url, Class<T> entityClass) {
        // @formatter:off
        // 设置请求URL和请求头，发送请求并获取响应体，最后将响应体解析为指定类型的对象
        return getRestClient().post()
                .uri(url)
                .headers(getJsonContentHeaders())
                // 发送GET请求并获取响应
                .retrieve()
                .toEntity(entityClass)
                .getBody();
        // @formatter:on
    }
}
