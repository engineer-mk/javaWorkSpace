package org.example.beans;

import org.springframework.context.annotation.*;

/**
 * @author makui
 * @created on  2022/9/27
 **/
@EnableAspectJAutoProxy
@ComponentScan(value = "org.example.beans.*")
@Configuration
public class MyConfig {
    @Bean
    public UserA userA() {
        return new UserA();
    }

    @Bean
    public UserB userB() {
        return new UserB();
    }

}
