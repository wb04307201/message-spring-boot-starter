package cn.wubo.message.util;

import cn.wubo.message.message.*;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ContentUtils {

    private ContentUtils() {
    }

    public static String toMarkdown(MarkdownContent content) {
        return toMarkdown(content, "\n\n");
    }

    public static String toMarkdown(MarkdownContent content, String join) {
        return content.getLines().stream().map(line -> {
            switch (line.getLineType()) {
                case TITLE:
                    SubTitleLine subTitleLine = (SubTitleLine) line;
                    return IntStream.range(0, subTitleLine.getLevel()).mapToObj(i -> "#").collect(Collectors.joining()) + " " + subTitleLine.getContent();
                case LINK:
                    SubLinkLine subLinkLine = (SubLinkLine) line;
                    return String.format("[%s](%s)", subLinkLine.getContent(), subLinkLine.getLink());
                case QUOTE:
                    SubQuoteLine subQuoteLine = (SubQuoteLine) line;
                    return String.format("> %s", subQuoteLine.getContent());
                case BOLD:
                    SubBoldLine subBoldLine = (SubBoldLine) line;
                    return String.format("**%s**", subBoldLine.getContent());
                case TEXT:
                default:
                    return line.getContent();
            }
        }).collect(Collectors.joining(join));
    }

    public static JSONArray toPost(MarkdownContent content) {
        JSONArray ja = new JSONArray();
        content.getLines().stream().forEach(line -> ja.add(switch (line.getLineType()) {
            case LINK -> {
                JSONObject temp = new JSONObject();
                SubLinkLine subLinkLine = (SubLinkLine) line;
                temp.put("tag", "a");
                temp.put("text", subLinkLine.getContent());
                temp.put("href", subLinkLine.getLink());
                yield temp;
            }
            case BOLD -> {
                JSONObject temp = new JSONObject();
                SubBoldLine subBoldLine = (SubBoldLine) line;
                temp.put("tag", "text");
                temp.put("text", subBoldLine.getContent());
                temp.put("style", new JSONArray().add("bold"));
                yield temp;
            }
            case TEXT, TITLE, QUOTE -> {
                JSONObject temp = new JSONObject();
                temp.put("tag", "text");
                temp.put("text", line.getContent());
                yield temp;
            }
        }));
        return ja;
    }

    public static String toHTML(MarkdownContent content) {
        return HtmlRenderer.builder().build().render(Parser.builder().build().parse(toMarkdown(content, "  ")));
    }
}
