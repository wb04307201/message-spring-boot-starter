package cn.wubo.message.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.codec.binary.Base64;
import org.springframework.util.Assert;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class FeishuUtils {

    private FeishuUtils() {
    }

    private static final String WEBHOOK = "https://open.feishu.cn/open-apis/bot/v2/hook/%s";

    /**
     * 向定制机器人发送请求。
     *
     * @param hookid 机器人的hookid，用于标识特定的机器人。
     * @param body   发送给机器人的消息体。
     * @return 返回从机器人接收到的响应内容。
     */
    public static String customRobot(String hookid, CustomRobotRequest body) {
        // 使用RestClientUtils的post方法，向指定webhook地址发送post请求
        return RestClientUtils.post(String.format(WEBHOOK, hookid), body);
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class CustomRobotRequest {
        private @JsonProperty("msg_type") String msgType;
        private @JsonProperty("content") Object content;
        private @JsonProperty("timestamp") Long timeStamp;
        private @JsonProperty("sign") String sign;

        public CustomRobotRequest(String msgType, Long timeStamp, String sign) {
            this.msgType = msgType;
            this.timeStamp = timeStamp;
            this.sign = sign;
        }

        public static Builder builder(String msgType, Long timeStamp, String sign) {
            return new Builder(msgType, timeStamp, sign);
        }

        public static class Builder {
            CustomRobotRequest options;

            public Builder(String msgType, Long timeStamp, String sign) {
                this.options = new CustomRobotRequest(msgType, timeStamp, sign);
            }

            public Builder withPost(String title, List<PostContenDetail> content) {
                Assert.isTrue(!"markdown".equals(this.options.msgType), " msgType value is not markdown, should not use this method!");
                this.options.content = new PostContent(new Post(new ZhCn(title, content)));
                return this;
            }

            public Builder withPost(List<PostContenDetail> content) {
                return withPost(null, content);
            }

            public Builder withText(TextContent text) {
                Assert.isTrue(!"text".equals(this.options.msgType), " msgType value is not text, should not use this method!");
                this.options.content = text;
                return this;
            }

            public CustomRobotRequest build() {
                return this.options;
            }

        }

        public record TextContent(String text) {
        }

        public record PostContent(Post post) {
        }

        public record Post(@JsonProperty("zh_cn") ZhCn zhCn) {
        }

        public record ZhCn(String title, List<PostContenDetail> content) {
        }

        public record PostContenDetail(String tag, String text, String href, @JsonProperty("user_id") String userId, @JsonProperty("user_name") String userName) {
        }
    }
}
