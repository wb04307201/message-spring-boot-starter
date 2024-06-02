package cn.wubo.message.platform;

import cn.wubo.message.core.MessageBase;
import cn.wubo.message.core.MessageType;

import java.lang.reflect.InvocationTargetException;

public class SendFactory {

    private SendFactory() {
    }

    public static <T extends ISendService<R>, R extends MessageBase> String run(R messageBase, Object content) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<T> clazz = MessageType.getClass(messageBase);
        ISendService<R> sendService = clazz.getConstructor().newInstance();
        return sendService.send(messageBase, content);
    }
}
