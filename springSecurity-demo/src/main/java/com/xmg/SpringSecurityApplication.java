package com.xmg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Arrays;

/**
 * @author makui
 * @created 2023/3/9
 **/
@SpringBootApplication
public class SpringSecurityApplication {
    public static void main(String[] args) {
        final ConfigurableApplicationContext context = SpringApplication.run(SpringSecurityApplication.class, args);
        final String[] beanNamesForType = context.getBeanNamesForType(SecurityFilterChain.class);
        System.out.println(Arrays.toString(beanNamesForType));
    }
}
