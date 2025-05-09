package com.bite.user;

import org.junit.jupiter.api.Test;
import org.springframework.boot.system.JavaVersion;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.*;
import org.springframework.core.type.AnnotatedTypeMetadata;

@SpringBootTest
public class ConditionalTest {
    @Test
    void test(){
        System.out.println("执行test方法");
}
}
@Configuration
class AppConfig{
    //如果Jdk17Conditional.matches返回true，则注册该Bean
    @Bean
    @Conditional(Jdk17Conditional.class)
    public JDK17 jdk17(){
        System.out.println("JDK17 初始化了");
        return new JDK17();
    }
    @Bean
    @Conditional(Jdk21Conditional.class)
    public JDK21 jdk21(){
        System.out.println("JDK21 初始化了");
        return new JDK21();
    }
}
class JDK17{}
class JDK21{}
class Jdk17Conditional implements Condition{

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        return JavaVersion.getJavaVersion().equals(JavaVersion.SEVENTEEN);
    }
    }
    class Jdk21Conditional implements Condition{
        @Override
        public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
            return JavaVersion.getJavaVersion().equals(JavaVersion.TWENTY_ONE);
        }
    }

