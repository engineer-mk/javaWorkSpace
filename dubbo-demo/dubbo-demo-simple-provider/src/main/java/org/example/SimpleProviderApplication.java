package org.example;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author ${USER}
 * @created ${DATE}
 **/
@EnableDubbo
@SpringBootApplication
public class SimpleProviderApplication {
    public static void main(String[] args) {
        SpringApplication.run(SimpleProviderApplication.class, args);
    }
}
