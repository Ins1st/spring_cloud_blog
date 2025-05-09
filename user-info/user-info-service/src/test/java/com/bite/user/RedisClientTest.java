package com.bite.user;

import com.bite.common.utils.Redis;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RedisClientTest {
    @Autowired
    private Redis redis;

    @Test
    void test(){
    redis.set("aa","ee");
    }
}
