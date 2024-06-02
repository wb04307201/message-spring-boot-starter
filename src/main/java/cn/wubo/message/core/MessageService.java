package cn.wubo.message.core;

import cn.wubo.message.message.RequestContent;
import cn.wubo.message.platform.SendFactory;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
public class MessageService {
    CopyOnWriteArrayList<MessageBase> aliases;

    public MessageService(List<MessageBase> aliases) {
        this.aliases = new CopyOnWriteArrayList<>(aliases);
    }

    /**
     * 发送请求内容
     *
     * @param content 请求内容，包含别名和消息类型等信息
     * @return 发送结果列表，列表中可能包含成功发送的消息或发送失败的错误信息
     */
    public List<String> send(RequestContent<?> content) {
        List<String> strings = new ArrayList<>();

        // 根据请求内容中的别名和消息类型过滤符合条件的项，并尝试发送消息
        aliases.stream().filter(item -> {
            // 过滤别名条件
            if (content.getAlias().isEmpty()) return true;
            else return content.getAlias().contains(item.getAlias());
        }).filter(item -> {
            // 过滤消息类型条件
            if (content.getMessageType().isEmpty()) return true;
            else return content.getMessageType().contains(MessageType.getMessageType(item));
        }).forEach(item -> {
            try {
                // 尝试根据发送工厂类发送消息，并将结果添加到结果列表中
                strings.add(SendFactory.run(item, content));
            } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                     IllegalAccessException e) {
                // 捕获并记录发送过程中的异常，将异常信息添加到结果列表中
                log.error(e.getMessage(), e);
                strings.add(e.getMessage());
            }
        });

        return strings;
    }

    /**
     * 添加一个新的消息基本信息到列表中。如果列表中已经存在具有相同别名的消息，则先移除该消息，然后添加新的消息。
     *
     * @param messageBase 要添加的消息基本信息对象，包含消息的别名等信息。
     */
    public synchronized void add(MessageBase messageBase) {
        // 从列表中移除具有相同别名的旧消息，确保列表中不会有重复的别名消息
        aliases.stream().filter(e -> e.getAlias().equals(messageBase.getAlias())).findAny().ifPresent(e -> aliases.remove(e));
        // 添加新的消息基本信息到列表
        aliases.add(messageBase);
    }

    /**
     * 根据别名移除对象
     * 从别名集合中查找与给定别名相匹配的对象，如果找到，则从集合中移除该对象。
     *
     * @param alias 别名 用于查找和移除对象的依据。
     */
    public synchronized void removeByAlias(String alias) {
        // 使用流式操作对别名列表进行过滤，并查找与给定别名相匹配的元素
        aliases.stream().filter(e -> e.getAlias().equals(alias)).findAny().ifPresent(e -> aliases.remove(e));
    }

}
