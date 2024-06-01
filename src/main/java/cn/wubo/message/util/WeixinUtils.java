package cn.wubo.message.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.util.Assert;

public class WeixinUtils {

    private WeixinUtils() {
    }

    private static final String WEBHOOK = "https://qyapi.weixin.qq.com/cgi-bin/webhook/send?key=%s";
    private static final String GET_TOKEN = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=%s&corpsecret=%s";
    private static final String MESSAGE = "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=%s";

    /**
     * 向自定义机器人发送请求。
     * 该方法通过POST请求向指定的Webhook地址发送消息体。
     *
     * @param key  自定义机器人的Key，用于构建Webhook地址。
     * @param body 要发送的消息体内容。
     * @return RestClientUtils.post返回的响应结果，通常为字符串形式的响应体。
     */
    public static String customRobot(String key, CustomRobotRequest body) {
        // 使用Key格式化Webhook地址，并发送POST请求
        return RestClientUtils.post(String.format(WEBHOOK, key), body);
    }

    /**
     * 获取企业微信的访问令牌。
     *
     * @param corpid     企业ID，用于标识企业微信账号。
     * @param corpsecret 企业微信的密钥，用于验证身份。
     * @return 返回从企业微信服务器获取的访问令牌。
     */
    private static String getToken(String corpid, String corpsecret) {
        // 尝试从缓存中获取令牌
        String token = CaffieneCache.getToken("weixin");
        if (token == null) {
            // 如果缓存中未找到令牌，则通过网络请求从企业微信服务器获取
            WeixinToken weixinToken = RestClientUtils.get(String.format(GET_TOKEN, corpid, corpsecret), WeixinToken.class);
            Assert.isTrue(weixinToken.errcode() != 0, "get weixin token is failed!");
            // 将获取到的令牌及其过期时间存入缓存
            token = CaffieneCache.setToken("weixin", weixinToken.expiresIn(), weixinToken.accessToken());
        }
        return token;
    }

    /**
     * 发送企业消息。
     *
     * @param corpid     企业的ID，用于身份验证。
     * @param corpsecret 企业的应用secret，用于获取访问令牌。
     * @param body       消息的内容，将被发送给指定的企业。
     * @return 返回发送消息后接收到的响应内容。
     * <p>
     * 此方法内部通过调用getToken获取访问令牌，并将其格式化到消息发送的URL中，
     * 随后使用RestClientUtils的post方法发送POST请求。
     */
    public static String message(String corpid, String corpsecret, MessageRobotRequest body) {
        return RestClientUtils.post(String.format(MESSAGE, getToken(corpid, corpsecret)), body);
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class CustomRobotRequest {

        private @JsonProperty("msgtype") String msgType;
        private @JsonProperty("markdown") Content markdown;
        private @JsonProperty("text") Content text;

        public CustomRobotRequest(String msgType) {
            this.msgType = msgType;
        }

        public static Builder builder(String msgType) {
            return new Builder(msgType);
        }

        public static class Builder {
            CustomRobotRequest options;

            public Builder(String msgType) {
                this.options = new CustomRobotRequest(msgType);
            }

            public Builder withMarkdown(String markdown) {
                Assert.isTrue(!"markdown".equals(this.options.msgType), " msgType value is not markdown, should not use this method!");
                this.options.markdown = new Content(markdown);
                return this;
            }

            public Builder withText(String text) {
                Assert.isTrue(!"text".equals(this.options.msgType), " msgType value is not text, should not use this method!");
                this.options.text = new Content(text);
                return this;
            }

            public CustomRobotRequest build() {
                return this.options;
            }
        }

        public record Content(String content) {
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class MessageRobotRequest {
        private @JsonProperty("msgtype") String msgType;
        private @JsonProperty("agentid") String agentId;
        private @JsonProperty("markdown") Content markdown;
        private @JsonProperty("text") Content text;
        private @JsonProperty("touser") String toUser;
        private @JsonProperty("toparty") String toParty;
        private @JsonProperty("totag") String toTag;

        public MessageRobotRequest(String msgType, String agentId) {
            this.msgType = msgType;
            this.agentId = agentId;
        }

        public static Builder builder(String msgType, String agentId) {
            return new Builder(msgType, agentId);
        }

        public static class Builder {
            MessageRobotRequest options;

            public Builder(String msgType, String agentId) {
                this.options = new MessageRobotRequest(msgType, agentId);
            }

            public Builder withMarkdown(String markdown) {
                Assert.isTrue(!"markdown".equals(this.options.msgType), " msgType value is not markdown, should not use this method!");
                this.options.markdown = new Content(markdown);
                return this;
            }

            public Builder withText(String text) {
                Assert.isTrue(!"text".equals(this.options.msgType), " msgType value is not text, should not use this method!");
                this.options.text = new Content(text);
                return this;
            }

            public Builder withToUser(String toUser) {
                this.options.toUser = toUser;
                return this;
            }

            public Builder withToParty(String toParty) {
                this.options.toParty = toParty;
                return this;
            }

            public Builder withToTag(String toTag) {
                this.options.toTag = toTag;
                return this;
            }

            public MessageRobotRequest build() {
                return this.options;
            }
        }

        public record Content(String content) {
        }
    }

    public record WeixinToken(
            // 出错返回码，为0表示成功，非0表示调用失败
            Integer errcode,
            // 返回码提示语
            String errmsg,
            // 	获取到的凭证，最长为512字节
            @JsonProperty("access_token") String accessToken,
            // 	凭证的有效时间（秒）
            @JsonProperty("expires_in") Long expiresIn) {
    }
}
