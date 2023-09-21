package cn.wubo.message.platform;

import cn.wubo.message.core.MessageBase;
import cn.wubo.message.core.MessageType;
import cn.wubo.message.record.IMessageRecordService;

import java.lang.reflect.InvocationTargetException;

public class SendFactory {

    public static String run(MessageBase messageBase, Object content, IMessageRecordService messageRecordService) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<? extends ISendService> clazz = MessageType.getClass(messageBase);
        ISendService sendService = clazz.getConstructor(IMessageRecordService.class).newInstance(messageRecordService);
        return sendService.send(messageBase, content);
    }
}
