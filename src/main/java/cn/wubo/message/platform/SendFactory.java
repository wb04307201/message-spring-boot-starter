package cn.wubo.message.platform;

import cn.wubo.message.core.MessageBase;
import cn.wubo.message.core.MessageType;
import cn.wubo.message.record.IMessageRecordService;

import java.lang.reflect.InvocationTargetException;

public class SendFactory {

    private SendFactory() {
    }

    public static <T extends ISendService<R>, R extends MessageBase> String run(R messageBase, Object content, IMessageRecordService messageRecordService) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<T> clazz = MessageType.getClass(messageBase);
        ISendService<R> sendService = clazz.getConstructor(IMessageRecordService.class).newInstance(messageRecordService);
        return sendService.send(messageBase, content);
    }
}
