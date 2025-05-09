package com.bite.user.listener;

import com.bite.common.constant.Constants;

import com.bite.common.utils.JsonUtil;
import com.bite.common.utils.Mail;
import com.bite.user.dataobject.UserInfo;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class UserQueueListener {
    @Autowired
    private Mail mail;
//    @RabbitListener(queues = Constants.USER_QUEUE_NAME)
//    public void handler(Message message, Channel channel) throws IOException {
//        long deliveryTag = message.getMessageProperties().getDeliveryTag();
//    try{
//        String body = new String(message.getBody());
//        log.info("收到用户信息,body:{}",body);
//        //TODO  发送注册成功邮件
//        //确认
//        channel.basicAck(deliveryTag,true);
//    }catch (Exception e){
//        //否定确认
//        channel.basicNack(deliveryTag,true,true);
//        log.error("邮件发送失败，e：",e);
//    }
//
//
//    }
//}
@RabbitListener(bindings = @QueueBinding(
        value = @Queue(value = Constants.USER_QUEUE_NAME,durable = "true"),
        exchange = @Exchange(value = Constants.USER_EXCHANGE_NAME,type = ExchangeTypes.FANOUT)
))
public void handler(Message message, Channel channel) throws IOException {
    long deliveryTag = message.getMessageProperties().getDeliveryTag();
    try{
        String body = new String(message.getBody());
        log.info("收到用户信息,body:{}",body);
        //TODO  发送注册成功邮件
        UserInfo userInfo = JsonUtil.parseJson(body,UserInfo.class);
        mail.send(userInfo.getEmail(),"欢迎加入比特博客社区",buildContent((userInfo.getUserName())));
        //确认
        channel.basicAck(deliveryTag,true);
    }catch (Exception e){
        //否定确认
        channel.basicNack(deliveryTag,true,true);
        log.error("邮件发送失败，e：",e);
    }
}
private String buildContent(String userName) {
    StringBuilder builder = new StringBuilder();
    builder.append("尊敬的").append(userName).append("，您好！").append(("<br/>"));
    builder.append("感谢您注册成为我们博客社区的一员！我们很高兴您加入我们的大家庭！<br/>");
    builder.append("您的注册信息如下：用户名：").append(userName).append("<br/");
    builder.append("为了确保您的账⼾安全，请妥善保管您的登录信息. 如果使⽤过程中, 遇到任何 问题, 欢迎联系我们的⽀持团队. XXXX@bite.com <br/>");
    builder.append("再次感谢您的加⼊，我们期待看到您的精彩内容！<br/>");
    builder.append("最好的祝愿<br/>");
    builder.append("⽐特博客团队");
    return builder.toString();
}
}
