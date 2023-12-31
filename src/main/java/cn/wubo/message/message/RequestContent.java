package cn.wubo.message.message;

import cn.wubo.message.core.MessageType;
import lombok.Getter;

import java.util.*;

@Getter
public abstract class RequestContent<T> {
    protected List<String> alias = new ArrayList<>();
    protected List<MessageType> messageType = new ArrayList<>();
    protected ContentParams params = new ContentParams();
    protected Map<String, ContentParams> aliasParams = new HashMap<>();

    public static TextContent buildText() {
        return new TextContent();
    }

    public static MarkdownContent buildMarkdown() {
        return new MarkdownContent();
    }

    public T addAlias(String... alias) {
        Collections.addAll(this.alias, alias);
        return (T) this;
    }

    public T addMessageType(MessageType... messageType) {
        Collections.addAll(this.messageType, messageType);
        return (T) this;
    }

    public T addDingtalkCustomRobot(String key, Object value) {
        params.getDingtalkCustomRobot().put(key, value);
        return (T) this;
    }

    public T addDingtalkMessage(String key, Object value) {
        params.getDingtalkMessage().put(key, value);
        return (T) this;
    }

    public T addFeishuCustomRobot(String key, Object value) {
        params.getFeishuCustomRobot().put(key, value);
        return (T) this;
    }

    public T addFeishuMessage(String key, Object value) {
        params.getFeishuMessage().put(key, value);
        return (T) this;
    }

    public T addWeixinMessage(String key, Object value) {
        params.getWeixinMessage().put(key, value);
        return (T) this;
    }

    public T addMailSmtp(String key, Object value) {
        params.getMailSmtp().put(key, value);
        return (T) this;
    }

    public T addDingtalkCustomRobot(String alias, String key, Object value) {
        aliasParams.computeIfAbsent(alias, s -> new ContentParams()).getDingtalkCustomRobot().put(key, value);
        return (T) this;
    }

    public T addDingtalkMessage(String alias, String key, Object value) {
        aliasParams.computeIfAbsent(alias, s -> new ContentParams()).getDingtalkMessage().put(key, value);
        return (T) this;
    }

    public T addFeishuCustomRobot(String alias, String key, Object value) {
        aliasParams.computeIfAbsent(alias, s -> new ContentParams()).getFeishuCustomRobot().put(key, value);
        return (T) this;
    }

    public T addFeishuMessage(String alias, String key, Object value) {
        aliasParams.computeIfAbsent(alias, s -> new ContentParams()).getFeishuMessage().put(key, value);
        return (T) this;
    }

    public T addWeixinMessage(String alias, String key, Object value) {
        aliasParams.computeIfAbsent(alias, s -> new ContentParams()).getWeixinMessage().put(key, value);
        return (T) this;
    }

    public T addMailSmtp(String alias, String key, Object value) {
        aliasParams.computeIfAbsent(alias, s -> new ContentParams()).getMailSmtp().put(key, value);
        return (T) this;
    }

    public ContentParams getContentParams(String alias) {
        if (aliasParams.containsKey(alias)) return aliasParams.get(alias);
        return params;
    }
}
