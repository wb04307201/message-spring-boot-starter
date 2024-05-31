package cn.wubo.message.util;

import org.springframework.web.client.RestClient;

public class RestClientUtils {

    private static RestClient restClient;

    private RestClientUtils() {
    }

    public static synchronized RestClient getRestClient() {
        if (restClient == null) return RestClient.builder().build();
        return restClient;
    }
}
