package cn.wubo.message.record.impl;

import cn.wubo.message.record.IMessageRecordService;
import cn.wubo.message.record.MessageRecord;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class MemMessageRecordServiceImpl implements IMessageRecordService {

    private static List<MessageRecord> messageRecordList = new ArrayList<>();

    @Override
    public MessageRecord save(MessageRecord messageRecord) {
        if (StringUtils.hasLength(messageRecord.getId())) {
            messageRecordList.stream().filter(e -> e.getId().equals(messageRecord.getId())).findAny().ifPresent(e -> e = messageRecord);
        } else {
            messageRecord.setId(UUID.randomUUID().toString());
            messageRecordList.add(messageRecord);
        }
        return messageRecord;
    }

    @Override
    public List<MessageRecord> list(MessageRecord messageRecord) {
        return messageRecordList.stream().filter(e -> !StringUtils.hasLength(messageRecord.getAlias()) || messageRecord.getAlias().contains(e.getAlias())).filter(e -> !StringUtils.hasLength(messageRecord.getContent()) || e.getContent().contains(messageRecord.getContent())).filter(e -> !StringUtils.hasLength(messageRecord.getResponse()) || e.getResponse().contains(messageRecord.getResponse())).collect(Collectors.toList());
    }

    @Override
    public void init() {
    }
}
