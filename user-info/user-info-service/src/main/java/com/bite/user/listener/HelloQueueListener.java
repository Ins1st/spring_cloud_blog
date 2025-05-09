package com.bite.user.listener;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class HelloQueueListener {
    @RabbitListener(queues = "hello")
    public void handler(Message message) {
        System.out.println("收到消息："+message);
    }
}
