package com.bite.user;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import java.io.UnsupportedEncodingException;

@SpringBootTest
public class MailTest {
    @Autowired
    private JavaMailSender javaMailSender;
    @Test
    void send() throws MessagingException, UnsupportedEncodingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false);
        helper.setFrom("2230921218@qq.com","比特博客社区");
        helper.setTo("180603209@qq.com");
        helper.setSubject("测试邮件发送");
        helper.setText("<h1>用户注册成功</h1>",true);

        javaMailSender.send(mimeMessage);
    }
}
