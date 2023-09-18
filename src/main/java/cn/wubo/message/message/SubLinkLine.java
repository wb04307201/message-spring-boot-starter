package cn.wubo.message.message;

import lombok.Getter;

public class SubLinkLine extends SubLine {

    public SubLinkLine(String content, String link) {
        this.content = content;
        this.link = link;
        this.lineType = SubLineEnum.LINK;
    }

    @Getter
    private String link;
}
