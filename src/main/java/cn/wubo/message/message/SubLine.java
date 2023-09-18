package cn.wubo.message.message;

import lombok.Data;

@Data
public abstract class SubLine {
    protected SubLineEnum lineType;
    protected String content;

    public static SubLinkLine link(String content, String link) {
        return new SubLinkLine(content, link);
    }

    public static SubTextLine text(String content) {
        return new SubTextLine(content);
    }

    public static SubTitleLine title(String content, Integer level) {
        return new SubTitleLine(content, level);
    }

    public static SubTitleLine title(String content) {
        return new SubTitleLine(content, 1);
    }

    public static SubQuoteLine quote(String content) {
        return new SubQuoteLine(content);
    }

    public static SubBoldLine bold(String content) {
        return new SubBoldLine(content);
    }
}
