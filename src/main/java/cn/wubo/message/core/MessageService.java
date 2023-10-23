package cn.wubo.message.core;

import cn.wubo.message.message.RequestContent;
import cn.wubo.message.platform.SendFactory;
import cn.wubo.message.record.IMessageRecordService;
import cn.wubo.message.record.MessageRecord;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
public class MessageService {
    CopyOnWriteArrayList<MessageBase> aliases;
    IMessageRecordService messageRecordService;

    public MessageService(List<MessageBase> aliases, IMessageRecordService messageRecordService) {
        this.aliases = new CopyOnWriteArrayList<>(aliases);
        this.messageRecordService = messageRecordService;
    }

    public List<String> send(RequestContent<?> content) {
        List<String> strings = new ArrayList<>();
        aliases.stream().filter(item -> {
            if (content.getAlias().isEmpty()) return true;
            else return content.getAlias().contains(item.getAlias());
        }).filter(item -> {
            if (content.getMessageType().isEmpty()) return true;
            else return content.getMessageType().contains(MessageType.getMessageType(item));
        }).forEach(item -> {
            try {
                strings.add(SendFactory.run(item, content, messageRecordService));
            } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                     IllegalAccessException e) {
                log.error(e.getMessage(), e);
                strings.add(e.getMessage());
            }
        });
        return strings;
    }

    public void add(MessageBase messageBase) {
        aliases.stream().filter(e -> e.getAlias().equals(messageBase.getAlias())).findAny().ifPresent(e -> aliases.remove(e));
        aliases.add(messageBase);
    }

    public void removeByAlias(String alias) {
        aliases.stream().filter(e -> e.getAlias().equals(alias)).findAny().ifPresent(e -> aliases.remove(e));
    }

    public List<MessageRecord> list(MessageRecord messageRecord){
        return messageRecordService.list(messageRecord);
    }
}
