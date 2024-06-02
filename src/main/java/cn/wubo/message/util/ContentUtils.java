package cn.wubo.message.util;

import cn.wubo.message.message.*;
import cn.wubo.message.platform.feishu.FeishuUtils;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import java.util.List;
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
     * @param join    连接符，用于行与行之间的连接。
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
     * 将Markdown内容转换为飞书机器人可识别的Post内容详情列表。
     *
     * @param content Markdown内容对象，包含多行文本数据。
     * @return 返回一个FeishuUtils.CustomRobotRequest.PostContenDetail对象的列表，
     * 其中每个对象代表飞书消息中的一个元素（如文本、链接等）。
     */
    public static List<FeishuUtils.CustomRobotRequest.PostContenDetail> toPost(MarkdownContent content) {
        // 遍历Markdown内容的每一行，并根据行类型转换为飞书消息格式
        return content.getLines().stream().map(line -> switch (line.getLineType()) {
            case LINK -> { // 当行为链接类型时
                SubLinkLine subLinkLine = (SubLinkLine) line;
                // 转换为飞书消息中的链接元素
                yield new FeishuUtils.CustomRobotRequest.PostContenDetail("a", subLinkLine.getContent(), subLinkLine.getLink(), null, null);
            }
            case TEXT, TITLE, QUOTE, BOLD -> { // 当行为文本、标题、引用或加粗类型时
                SubBoldLine subLinkLine = (SubBoldLine) line;
                // 转换为飞书消息中的文本元素
                yield new FeishuUtils.CustomRobotRequest.PostContenDetail("text", subLinkLine.getContent(), null, null, null);
            }
        }).toList();
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
