package cn.wubo.message.core;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MailProperties {

    private List<Smtp> smtp = new ArrayList<>();

    @Data
    @Builder
    public static class Smtp extends MessageBase {
        private String host;
        private String from;
        private String username;
        private String password;
    }
}
