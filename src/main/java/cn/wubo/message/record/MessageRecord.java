package cn.wubo.message.record;

import lombok.Data;

import java.util.Date;

@Data
public class MessageRecord {
    private String id;
    private String alias;
    private String type;
    private String content;
    private String response;
    private Date createTime;
}
