package cn.wubo.message.platform;

import cn.wubo.message.core.MessageBase;
import cn.wubo.message.message.MarkdownContent;
import cn.wubo.message.message.TextContent;

import java.util.Map;

public interface ISendService<T extends MessageBase> {

    String send(T aliasProperties, Object content);

    String sendMarkdown(T aliasProperties, MarkdownContent content);

    String sendText(T aliasProperties, TextContent content);
}
