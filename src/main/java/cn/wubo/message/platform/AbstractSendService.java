package cn.wubo.message.platform;

import cn.wubo.message.core.MessageBase;
import cn.wubo.message.message.MarkdownContent;
import cn.wubo.message.message.TextContent;
import cn.wubo.message.record.IMessageRecordService;
import cn.wubo.message.record.MessageRecord;
import com.alibaba.fastjson.JSON;

import java.util.Map;

public abstract class AbstractSendService<T extends MessageBase> implements ISendService<T> {
    protected IMessageRecordService messageRecordService;

    protected AbstractSendService(IMessageRecordService messageRecordService) {
        this.messageRecordService = messageRecordService;
    }

    public String send(T aliasProperties, Object content) {
        MessageRecord messageRecord = this.beforeSend(JSON.toJSONString(content));
        String res;
        if (content instanceof MarkdownContent) {
            res = sendMarkdown(aliasProperties, (MarkdownContent) content);
        } else if (content instanceof TextContent) {
            res = sendText(aliasProperties, (TextContent) content);
        } else {
            throw new RuntimeException("");
        }
        messageRecord.setResponse(res);
        afterSend(messageRecord);
        return res;
    }

    private MessageRecord beforeSend(String content) {
        return null;
    }

    private void afterSend(MessageRecord messageRecord) {

    }
}
