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
    protected Map<MessageType, ContentParams> messageTypeParams = new HashMap<>();

    public static TextContent buildText() {
        return new TextContent();
    }

    public static MarkdownContent buildMarkdown() {
        return new MarkdownContent();
    }

    public T addAlias(String... alias) {
        this.alias.add(Arrays.toString(alias));
        return (T) this;
    }

    public T addMessageType(MessageType messageType) {
        this.messageType.add(messageType);
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

    public T addDingtalkCustomRobot(MessageType messageType, String key, Object value) {
        messageTypeParams.computeIfAbsent(messageType, s -> new ContentParams()).getDingtalkCustomRobot().put(key, value);
        return (T) this;
    }

    public T addDingtalkMessage(MessageType messageType, String key, Object value) {
        messageTypeParams.computeIfAbsent(messageType, s -> new ContentParams()).getDingtalkMessage().put(key, value);
        return (T) this;
    }

    public T addFeishuCustomRobot(MessageType messageType, String key, Object value) {
        messageTypeParams.computeIfAbsent(messageType, s -> new ContentParams()).getFeishuCustomRobot().put(key, value);
        return (T) this;
    }

    public T addFeishuMessage(MessageType messageType, String key, Object value) {
        messageTypeParams.computeIfAbsent(messageType, s -> new ContentParams()).getFeishuMessage().put(key, value);
        return (T) this;
    }

    public T addWeixinMessage(MessageType messageType, String key, Object value) {
        messageTypeParams.computeIfAbsent(messageType, s -> new ContentParams()).getWeixinMessage().put(key, value);
        return (T) this;
    }

    public T addMailSmtp(MessageType messageType, String key, Object value) {
        messageTypeParams.computeIfAbsent(messageType, s -> new ContentParams()).getMailSmtp().put(key, value);
        return (T) this;
    }

    public ContentParams getContentParams(String alias, MessageType messageType) {
        if (aliasParams.containsKey(alias)) return aliasParams.get(alias);
        if (messageTypeParams.containsKey(messageType)) return messageTypeParams.get(messageType);
        return params;
    }
}
