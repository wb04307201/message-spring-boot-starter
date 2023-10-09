package cn.wubo.message.util;

import org.springframework.util.StringUtils;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class MailUtils {

    private MailUtils() {
    }

    public static void smtp(String host, String from, String to, String username, String password, String title, String body) throws MessagingException {
        //1.创建参数配置, 用于连接邮件服务器的参数配置
        Properties props = new Properties();                    //参数配置
        props.setProperty("mail.transport.protocol", "smtp");   //使用的协议（JavaMail规范要求）
        props.setProperty("mail.smtp.host", host);              //发件人的邮箱的 SMTP 服务器地址
        props.setProperty("mail.smtp.auth", "true");            //需要请求认证

        //2.根据配置创建会话对象, 用于和邮件服务器交互
        Session session = Session.getInstance(props);
        //设置为debug模式,可以查看详细的发送log session.setDebug(true);

        //3.构造邮件
        MimeMessage message = new MimeMessage(session);
        message.setRecipients(Message.RecipientType.TO, to);
        if (StringUtils.hasText(title)) {
            message.setSubject(title);
            message.setContent(body, "text/html;charset=UTF-8");
        } else {
            message.setText(body);
        }
        message.setFrom(new InternetAddress(from));

        //4.根据 Session获取邮件传输对象
        Transport transport = session.getTransport();
        transport.connect(username, password);
        transport.sendMessage(message, message.getAllRecipients());
    }
}
