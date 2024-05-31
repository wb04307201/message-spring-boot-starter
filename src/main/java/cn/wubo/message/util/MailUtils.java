package cn.wubo.message.util;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.util.StringUtils;

import java.util.Properties;

public class MailUtils {

    private MailUtils() {
    }

    /**
     * 使用SMTP协议发送邮件的函数
     *
     * @param host 邮件服务器主机名或地址
     * @param from 发件人邮箱地址
     * @param to 收件人邮箱地址
     * @param username 发件人邮箱用户名（通常为邮箱地址）
     * @param password 发件人邮箱密码
     * @param title 邮件主题（如果为空，则不设置主题，邮件内容将作为纯文本发送）
     * @param body 邮件正文（支持HTML格式）
     * @throws MessagingException 如果邮件操作出现异常，则抛出此异常
     */
    public static void smtp(String host, String from, String to, String username, String password, String title, String body) throws MessagingException {
        // 1. 创建参数配置，用于连接邮件服务器的参数配置
        Properties props = new Properties();                    // 参数配置
        props.setProperty("mail.transport.protocol", "smtp");   // 使用的协议（JavaMail规范要求）
        props.setProperty("mail.smtp.host", host);              // 发件人的邮箱的 SMTP 服务器地址
        props.setProperty("mail.smtp.auth", "true");            // 需要请求认证

        // 2. 根据配置创建会话对象，用于和邮件服务器交互
        Session session = Session.getInstance(props);
        // 设置为debug模式,可以查看详细的发送log session.setDebug(true);

        // 3. 构造邮件
        MimeMessage message = new MimeMessage(session);
        message.setRecipients(Message.RecipientType.TO, to);
        if (StringUtils.hasText(title)) {
            message.setSubject(title);
            message.setContent(body, "text/html;charset=UTF-8"); // 设置邮件内容为HTML格式
        } else {
            message.setText(body); // 如果没有主题，则将邮件内容设置为纯文本
        }
        message.setFrom(new InternetAddress(from));

        // 4. 根据 Session获取邮件传输对象
        Transport transport = session.getTransport();
        transport.connect(username, password); // 使用用户名和密码连接邮件服务器
        transport.sendMessage(message, message.getAllRecipients()); // 发送邮件
    }

}
