package cn.wubo.message.platform;

import cn.wubo.message.core.MessageBase;
import cn.wubo.message.core.MessageType;
import cn.wubo.message.exception.MessageRuntimeException;
import cn.wubo.message.message.MarkdownContent;
import cn.wubo.message.message.TextContent;
import cn.wubo.message.record.IMessageRecordService;
import cn.wubo.message.record.MessageRecord;
import com.alibaba.fastjson.JSON;

import java.util.Date;

public abstract class AbstractSendService<T extends MessageBase> implements ISendService<T> {
    protected IMessageRecordService messageRecordService;

    protected AbstractSendService(IMessageRecordService messageRecordService) {
        this.messageRecordService = messageRecordService;
    }

    public String send(T aliasProperties, Object content) {
        MessageRecord messageRecord = this.beforeSend(MessageType.getMessageType(aliasProperties).name(), JSON.toJSONString(content));
        String res;
        if (content instanceof MarkdownContent) {
            res = sendMarkdown(aliasProperties, (MarkdownContent) content);
        } else if (content instanceof TextContent) {
            res = sendText(aliasProperties, (TextContent) content);
        } else {
            throw new MessageRuntimeException("未知的消息内容类型!");
        }
        messageRecord.setResponse(res);
        afterSend(messageRecord);
        return res;
    }

    private MessageRecord beforeSend(String aliasProperties, String content) {
        MessageRecord messageRecord = new MessageRecord();
        messageRecord.setAlias(aliasProperties);
        messageRecord.setContent(content);
        messageRecord.setCreateTime(new Date());
        return messageRecordService.save(messageRecord);
    }

    private void afterSend(MessageRecord messageRecord) {
        messageRecordService.save(messageRecord);
    }
}
