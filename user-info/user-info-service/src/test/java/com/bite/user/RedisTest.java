package com.bite.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
@SpringBootTest
public class RedisTest {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Test
    void test(){
        redisTemplate.opsForValue().set("hello","spring");
        String var = redisTemplate.opsForValue().get("hello");
        System.out.println(var);
    }
}
