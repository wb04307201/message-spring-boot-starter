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

    /**
     * 发送请求内容
     * @param content 请求内容
     * @return 发送结果列表
     */
    public List<String> send(RequestContent<?> content) {
        List<String> strings = new ArrayList<>();

        // 根据别名过滤符合条件的项
        aliases.stream().filter(item -> {
            if (content.getAlias().isEmpty()) return true;
            else return content.getAlias().contains(item.getAlias());
        }).filter(item -> {
            if (content.getMessageType().isEmpty()) return true;
            else return content.getMessageType().contains(MessageType.getMessageType(item));
        }).forEach(item -> {
            try {
                // 根据发送工厂类发送消息
                strings.add(SendFactory.run(item, content, messageRecordService));
            } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
                log.error(e.getMessage(), e);
                strings.add(e.getMessage());
            }
        });

        return strings;
    }


    /**
     * 添加一个新的消息基本信息到列表中
     *
     * @param messageBase 要添加的消息基本信息
     */
    public void add(MessageBase messageBase) {
        aliases.stream().filter(e -> e.getAlias().equals(messageBase.getAlias())).findAny().ifPresent(e -> aliases.remove(e));
        aliases.add(messageBase);
    }


    /**
     * 根据别名移除对象
     * @param alias 别名
     */
    public void removeByAlias(String alias) {
        aliases.stream().filter(e -> e.getAlias().equals(alias)).findAny().ifPresent(e -> aliases.remove(e));
    }


    /**
     * 查询消息记录列表
     *
     * @param messageRecord 消息记录对象
     * @return 消息记录列表
     */
    public List<MessageRecord> list(MessageRecord messageRecord){
        return messageRecordService.list(messageRecord);
    }

}
