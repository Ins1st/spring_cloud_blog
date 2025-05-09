package com.bite.common.utils;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import java.io.UnsupportedEncodingException;
import java.util.Optional;

public class Mail {
    @Autowired
    private JavaMailSender javaMailSender;
    private MailProperties mailProperties;

    public Mail(JavaMailSender javaMailSender,MailProperties mailProperties) {
        this.javaMailSender = javaMailSender;
        this.mailProperties = mailProperties;
    }

    public  void send(String to,String subject,String content) throws MessagingException, UnsupportedEncodingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false);
        String personal = Optional.ofNullable(mailProperties.getProperties().get("personal"))
                .orElse(mailProperties.getUsername());
        helper.setFrom(mailProperties.getUsername(),personal);
        helper.setTo(to);  //收件人
        helper.setSubject(subject);//邮箱主题
        helper.setText(content,true);

        javaMailSender.send(mimeMessage);
    }
}
