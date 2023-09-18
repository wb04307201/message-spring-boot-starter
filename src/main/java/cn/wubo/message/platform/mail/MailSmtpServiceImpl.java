package cn.wubo.message.platform.mail;

import cn.wubo.message.core.MailProperties;
import cn.wubo.message.message.MarkdownContent;
import cn.wubo.message.message.TextContent;
import cn.wubo.message.platform.AbstractSendService;
import cn.wubo.message.record.IMessageRecordService;
import cn.wubo.message.util.ContentUtils;
import cn.wubo.message.util.MailUtils;
import lombok.extern.slf4j.Slf4j;

import javax.mail.MessagingException;


@Slf4j
public class MailSmtpServiceImpl extends AbstractSendService<MailProperties.Smtp> {


    protected MailSmtpServiceImpl(IMessageRecordService messageRecordService) {
        super(messageRecordService);
    }

    @Override
    public String sendMarkdown(MailProperties.Smtp aliasProperties, MarkdownContent content) {
        try {
            MailUtils.smtp(aliasProperties.getHost(), aliasProperties.getFrom(), (String) content.getContentParams(aliasProperties.getAlias()).getMailSmtp().get("to"), aliasProperties.getUsername(), aliasProperties.getPassword(), content.getTitle(), ContentUtils.toHTML(content));
            return "success";
        } catch (MessagingException e) {
            log.error(e.getMessage(), e);
            return e.getMessage();
        }
    }

    @Override
    public String sendText(MailProperties.Smtp aliasProperties, TextContent content) {
        try {
            MailUtils.smtp(aliasProperties.getHost(), aliasProperties.getFrom(), (String) content.getContentParams(aliasProperties.getAlias()).getMailSmtp().get("to"), aliasProperties.getUsername(), aliasProperties.getPassword(), null, content.getText());
            return "success";
        } catch (MessagingException e) {
            log.error(e.getMessage(), e);
            return e.getMessage();
        }
    }
}
