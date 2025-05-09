package com.bite.common.utils;


import java.util.HashMap;
import java.util.Map;

class JWTUtilsTest {

    @org.junit.jupiter.api.Test
    void genJwt() {
        Map<String,Object>  claim = new HashMap<>();
        claim.put("id",1);
        claim.put("name","zhangsan");
        System.out.println(JWTUtils.genJwt(claim));
    }
}