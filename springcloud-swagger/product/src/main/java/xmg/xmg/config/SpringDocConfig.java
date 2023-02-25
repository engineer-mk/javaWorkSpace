package xmg.xmg.config;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author makui
 * @created 2023/2/24
 **/
@Configuration
public class SpringDocConfig {
    @Bean
    public GroupedOpenApi usersGroup() {
        return GroupedOpenApi.builder().group("product")
                .addOperationCustomizer((operation, handlerMethod) -> {
                    operation.addSecurityItem(new SecurityRequirement().addList("basicScheme"));
                    return operation;
                })
                .addOpenApiCustomiser(openApi -> openApi.info(new Info().title("Product API").version("1.0")))
                .packagesToScan("com.xmg")
                .build();
    }
}
