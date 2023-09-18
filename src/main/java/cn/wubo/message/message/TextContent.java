package cn.wubo.message.message;

import lombok.Getter;

@Getter
public class TextContent extends RequestContent<TextContent> {
    private String text;

    public TextContent text(String text) {
        this.text = text;
        return this;
    }
}
