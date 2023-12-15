package cn.wubo.message.message;

import lombok.Getter;

import java.util.Objects;

public class SubLinkLine extends SubLine {

    public SubLinkLine(String content, String link) {
        this.content = content;
        this.link = link;
        this.lineType = SubLineEnum.LINK;
    }

    @Getter
    private String link;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SubLinkLine that = (SubLinkLine) o;
        return Objects.equals(link, that.link);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), link);
    }
}
