package cn.wubo.message.platform.mail;

import cn.wubo.message.core.MailProperties;
import cn.wubo.message.exception.MailRuntimeException;
import cn.wubo.message.message.MarkdownContent;
import cn.wubo.message.message.TextContent;
import cn.wubo.message.platform.AbstractSendService;
import cn.wubo.message.util.ContentUtils;
import cn.wubo.message.util.MailUtils;
import jakarta.mail.MessagingException;
import org.springframework.util.Assert;

public class MailSmtpServiceImpl extends AbstractSendService<MailProperties.Smtp> {

    /**
     * 使用Markdown格式发送邮件。
     *
     * @param aliasProperties 包含邮件发送所需属性的配置对象，比如SMTP服务器信息。
     * @param content         包含邮件内容的Markdown格式对象，以及相关的邮件参数。
     * @return 返回发送邮件的结果，通常为一个表示成功或失败的状态码或信息。
     */
    @Override
    public String sendMarkdown(MailProperties.Smtp aliasProperties, MarkdownContent content) {
        // @formatter:off
        // 根据别名获取邮件接收者地址，将Markdown内容转换为HTML格式，并发送邮件
        return mail(
                aliasProperties,
                (String) content.getContentParams(aliasProperties.getAlias()).getMailSmtp().get("to"),
                content.getTitle(),
                ContentUtils.toHTML(content)
        );
        // @formatter:on
    }

    /**
     * 使用SMTP属性和文本内容发送电子邮件。
     * <p>
     * 该方法通过SMTP属性和文本内容来发送电子邮件。它首先从文本内容中提取收件人地址，
     * 然后调用mail方法来实际发送邮件。此方法特别适用于只需要发送纯文本邮件的场景。
     *
     * @param aliasProperties SMTP别名属性，包含邮件发送所需的配置信息。
     * @param content         包含邮件文本内容的对象。
     * @return 返回发送邮件的结果，通常是邮件发送状态的描述。
     */
    @Override
    public String sendText(MailProperties.Smtp aliasProperties, TextContent content) {
        // @formatter:off
        // 从content中获取邮件的收件人地址，并使用aliasProperties和邮件内容调用mail方法发送邮件
        return mail(
                aliasProperties,
                (String) content.getContentParams(aliasProperties.getAlias()).getMailSmtp().get("to"),
                null,
                content.getText()
        );
        // @formatter:on
    }

    /**
     * 使用SMTP方式发送邮件的函数。
     *
     * @param aliasProperties 包含SMTP服务器相关信息的MailProperties.Smtp对象，不能为null。
     *                        其中必须包含host（SMTP服务器地址）、from（发件人地址）、username（发件人账号）、
     *                        password（发件人密码）等信息。
     * @param to              收件人邮箱地址，不能为null。
     * @param title           邮件主题，不能为null。
     * @param content         邮件正文，不能为null。
     * @return 返回发送结果，成功返回"success"。
     * @throws MailRuntimeException 如果邮件发送过程中出现任何MessagingException异常，
     *                              将封装并抛出MailRuntimeException。
     */
    public String mail(MailProperties.Smtp aliasProperties, String to, String title, String content) {
        // 校验必须的参数信息
        Assert.notNull(aliasProperties.getHost(), "host should not be null!");
        Assert.notNull(aliasProperties.getFrom(), "from should not be null!");
        Assert.notNull(to, "to should not be null!");
        Assert.notNull(aliasProperties.getUsername(), "username should not be null!");
        Assert.notNull(aliasProperties.getPassword(), "password should not be null!");

        try {
            // @formatter:off
            // 尝试通过SMTP方式发送邮件
            MailUtils.smtp(
                    aliasProperties.getHost(),
                    aliasProperties.getFrom(),
                    to,
                    aliasProperties.getUsername(),
                    aliasProperties.getPassword(),
                    title, content
            );
            // @formatter:on
            return "success";
        } catch (MessagingException e) {
            // 发送邮件异常，抛出自定义异常
            throw new MailRuntimeException(e.getMessage(), e);
        }
    }
}
