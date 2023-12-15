package cn.wubo.message.message;

import lombok.Getter;

import java.util.Objects;

public class SubTitleLine extends SubLine {

    @Getter
    private Integer level;

    public SubTitleLine(String content, Integer level) {
        this.lineType = SubLineEnum.TITLE;
        this.content = content;
        this.level = level;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SubTitleLine that = (SubTitleLine) o;
        return Objects.equals(level, that.level);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), level);
    }
}
