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

    /**
     * 将MarkdownContent内容转换为Markdown格式。
     *
     * @param content MarkdownContent内容对象，包含多行不同类型的SubLine内容。
     * @param join 连接符，用于行与行之间的连接。
     * @return 转换后的Markdown字符串。
     */
    public static String toMarkdown(MarkdownContent content, String join) {
        // 使用流处理每行内容，并根据行的类型转换为相应的Markdown格式
        return content.getLines().stream().map(line -> {
            switch (line.getLineType()) {
                case TITLE:
                    // 根据标题级别生成相应的#号和内容
                    SubTitleLine subTitleLine = (SubTitleLine) line;
                    return IntStream.range(0, subTitleLine.getLevel()).mapToObj(i -> "#").collect(Collectors.joining()) + " " + subTitleLine.getContent();
                case LINK:
                    // 生成Markdown的链接格式
                    SubLinkLine subLinkLine = (SubLinkLine) line;
                    return String.format("[%s](%s)", subLinkLine.getContent(), subLinkLine.getLink());
                case QUOTE:
                    // 生成Markdown的引用格式
                    SubQuoteLine subQuoteLine = (SubQuoteLine) line;
                    return String.format("> %s", subQuoteLine.getContent());
                case BOLD:
                    // 生成Markdown的加粗格式
                    SubBoldLine subBoldLine = (SubBoldLine) line;
                    return String.format("**%s**", subBoldLine.getContent());
                case TEXT:
                default:
                    // 默认情况下直接返回内容
                    return line.getContent();
            }
        }).collect(Collectors.joining(join));
    }

    /**
     * 将Markdown内容转换为适用于前端展示的JSON数组格式。
     * 每个Markdown的行内容会被转换成一个JSON对象，根据行的类型（链接、加粗、普通文本、标题、引用），对象包含不同的标签和内容。
     *
     * @param content Markdown内容对象，包含多行文本及其类型的信息。
     * @return JSONArray 包含转换后行信息的JSON数组。
     */
    public static JSONArray toPost(MarkdownContent content) {
        JSONArray ja = new JSONArray();
        // 遍历Markdown内容的每一行，根据行类型转换为相应的JSON对象并添加到数组中
        content.getLines().stream().forEach(line -> ja.add(switch (line.getLineType()) {
            case LINK -> {
                JSONObject temp = new JSONObject();
                // 链接类型行的处理：转换为<a>标签的JSON对象
                SubLinkLine subLinkLine = (SubLinkLine) line;
                temp.put("tag", "a");
                temp.put("text", subLinkLine.getContent());
                temp.put("href", subLinkLine.getLink());
                yield temp;
            }
            case BOLD -> {
                JSONObject temp = new JSONObject();
                // 加粗类型行的处理：转换为带bold样式的文本JSON对象
                SubBoldLine subBoldLine = (SubBoldLine) line;
                temp.put("tag", "text");
                temp.put("text", subBoldLine.getContent());
                temp.put("style", new JSONArray().add("bold"));
                yield temp;
            }
            case TEXT, TITLE, QUOTE -> {
                JSONObject temp = new JSONObject();
                // 普通文本、标题、引用类型行的处理：转换为普通文本的JSON对象
                temp.put("tag", "text");
                temp.put("text", line.getContent());
                yield temp;
            }
        }));
        return ja;
    }

    /**
     * 将Markdown内容转换为HTML格式。
     *
     * @param content MarkdownContent对象，包含待转换的Markdown文本。
     * @return String 返回转换后的HTML字符串。
     */
    public static String toHTML(MarkdownContent content) {
        // 构建Markdown解析器并使用它来解析Markdown文本，然后将解析结果渲染为HTML
        return HtmlRenderer.builder().build().render(Parser.builder().build().parse(toMarkdown(content, "  ")));
    }
}
