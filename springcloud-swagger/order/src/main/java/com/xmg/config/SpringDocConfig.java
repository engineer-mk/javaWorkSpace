package com.xmg.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author makui
 * @created 2023/2/26
 **/
@Configuration
public class SpringDocConfig {
    @Bean
    public OpenAPI customOpenApi() {
        return new OpenAPI().info(new Info().title("Order API")
                .description("Order API")
                .version("v1.0.0")
                .contact(new Contact()
                        .name("mk")
                        .email("kuim123@gmail.com")));
    }
}
