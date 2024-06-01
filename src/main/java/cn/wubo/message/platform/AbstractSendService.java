package cn.wubo.message.platform;

import cn.wubo.message.core.MessageBase;
import cn.wubo.message.exception.MessageRuntimeException;
import cn.wubo.message.message.MarkdownContent;
import cn.wubo.message.message.TextContent;

public abstract class AbstractSendService<T extends MessageBase> implements ISendService<T> {

    public String send(T aliasProperties, Object content) {
        String res;
        if (content instanceof MarkdownContent mc) {
            res = sendMarkdown(aliasProperties, mc);
        } else if (content instanceof TextContent tc) {
            res = sendText(aliasProperties, tc);
        } else {
            throw new MessageRuntimeException("未知的消息内容类型!");
        }
        return res;
    }
}
