package cn.wubo.message.record;

import java.util.List;

public interface IMessageRecordService {

    /**
     * 保存
     *
     * @param messageRecord 发送记录
     * @return ChatbotHistory
     */
    MessageRecord save(MessageRecord messageRecord);

    /**
     * 查询
     *
     * @param messageRecord 查询信息
     * @return List<ChatbotHistory>
     */
    List<MessageRecord> list(MessageRecord messageRecord);

    /**
     * 初始化
     */
    void init();
}
