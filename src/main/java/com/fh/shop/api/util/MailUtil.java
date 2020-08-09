package com.fh.shop.api.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

//根据控制反转来将new对象交个springIoc来完成
@Component
public class MailUtil {
    /**
     20      * @param args
     21      * @throws Exception
     22      */

    @Value("${mail.host}")
    private String host;
    @Value("${mail.transport.protocol}")
    private String protocol;
    @Value("${mail.user}")
    private String user;
    @Value("${mail.from}")
    private String from;
    @Value("${mail.password}")
    private String password;


    public  void sendMail(String email,String title,String text){
        Properties prop = new Properties();
        prop.setProperty("mail.host", host);
        prop.setProperty("mail.transport.protocol", protocol);
        prop.setProperty("mail.smtp.auth", "true");
        //使用JavaMail发送邮件的5个步骤
        //1、创建session
        Session session = Session.getInstance(prop);
        //2、通过session得到transport对象
        Transport  ts = null;
        try {
            ts = session.getTransport();
            //3、使用邮箱的用户名和密码连上邮件服务器，发送邮件时，发件人需要提交邮箱的用户名和密码给smtp服务器，用户名和密码都通过验证之后才能够正常发送邮件给收件人。
            ts.connect(host, user, password);
            //4、创建邮件
            //创建邮件对象
            MimeMessage message = new MimeMessage(session);
            //指明邮件的发件人
            message.setFrom(new InternetAddress(from));
            //指明邮件的收件人，现在发件人和收件人是一样的，那就是自己给自己发
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(email));
            //邮件的标题
            message.setSubject(title);
            //邮件的文本内容
            message.setContent(text, "text/html;charset=UTF-8");
            //5、发送邮件
            ts.sendMessage(message, message.getAllRecipients());
            } catch (MessagingException e) {
            e.printStackTrace();
             } finally {
            if(ts!=null){
                try {
                    ts.close();
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            }
        }



    }

}
