package cn.wubo.message.message;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class MarkdownContent extends RequestContent<MarkdownContent> {
    private String title;
    List<SubLine> lines = new ArrayList<>();

    public MarkdownContent title(String title) {
        this.title = title;
        return this;
    }

    public MarkdownContent addLine(SubLine line) {
        this.lines.add(line);
        return this;
    }

    private ContentParams params = new ContentParams();
}
