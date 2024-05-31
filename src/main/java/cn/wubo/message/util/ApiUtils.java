package cn.wubo.message.util;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.function.Consumer;

public class ApiUtils {

	private ApiUtils() {
	}

	public static Consumer<HttpHeaders> getJsonContentHeaders() {
		return headers -> headers.setContentType(MediaType.APPLICATION_JSON);
	}

}
