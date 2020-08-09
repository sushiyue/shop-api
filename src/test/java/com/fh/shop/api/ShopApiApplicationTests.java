package com.fh.shop.api;

import com.fh.shop.api.mq.MQSend;
import com.fh.shop.api.util.MailUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import redis.clients.jedis.Jedis;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@SpringBootTest
public class ShopApiApplicationTests {

    @Autowired
    private MailUtil mailUtil;
    @Test
    public void contextLoads() {
    }

    @Autowired
    private MQSend mqSend;
    @Test
    public void  sendTest(){
            mqSend.sendGoodsMessage("你好");
    }
    /**
     20      * @param args
     21      * @throws Exception
     22      */
       @Test
    public static void t() throws Exception {

                Properties prop = new Properties();
                prop.setProperty("mail.host", "smtp.qq.com");
                prop.setProperty("mail.transport.protocol", "smtp");
                prop.setProperty("mail.smtp.auth", "true");
                 //使用JavaMail发送邮件的5个步骤
                 //1、创建session
                 Session session = Session.getInstance(prop);
                 //2、通过session得到transport对象
                 Transport ts = session.getTransport();
              //3、使用邮箱的用户名和密码连上邮件服务器，发送邮件时，发件人需要提交邮箱的用户名和密码给smtp服务器，用户名和密码都通过验证之后才能够正常发送邮件给收件人。
                 ts.connect("smtp.qq.com", "1020228008", "yzaouynlqaopbddg");
                //4、创建邮件
                 Message message = createSimpleMail(session);
                 //5、发送邮件
                 ts.sendMessage(message, message.getAllRecipients());
                ts.close();
           }

             /**
 46     * @Method: createSimpleMail
 47     * @Description: 创建一封只包含文本的邮件
 48     * @Anthor:孤傲苍狼
 49     *
 50     * @param session
 51     * @return
 52     * @throws Exception
 53     */

             public static MimeMessage createSimpleMail(Session session)
            throws Exception {
                //创建邮件对象
                 MimeMessage message = new MimeMessage(session);
                 //指明邮件的发件人
                 message.setFrom(new InternetAddress("1020228008@qq.com"));
                 //指明邮件的收件人，现在发件人和收件人是一样的，那就是自己给自己发
                 message.setRecipient(Message.RecipientType.TO, new InternetAddress("3452603469@qq.com"));
                 //邮件的标题
                 message.setSubject("只包含文本的简单邮件");
                 //邮件的文本内容
                 message.setContent("你好啊！", "text/html;charset=UTF-8");
                //返回创建好的邮件对象
                 return message;
             }

    @Test
    public void redisTest(){
        // 连接 Redis 数据库 , 获取连接对象

        Jedis jedis = new Jedis("192.168.91.133",7020,500);
        // 向 Redis 数据库写入数据
        jedis.set("foda","萨达");
        // 读取 Redis 数据库数据
        String name = jedis.get("foda");
        // 打印输出
        System.out.println(name);
    }
    @Test
    public void redisTest1(){
        // 连接 Redis 数据库 , 获取连接对象
        Jedis jedis = new Jedis("192.168.91.133",7020,500);
        jedis.del("foda");
        // 打印输出
    }

}


