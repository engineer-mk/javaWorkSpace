package com.xmg.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.AbstractSwaggerUiConfigProperties;
import org.springdoc.core.SwaggerUiConfigProperties;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * @author makui
 * @created 2023/2/24
 **/
@Configuration
public class SpringDocConfig {
    protected final SwaggerUiConfigProperties swaggerUiConfigProperties;
    protected final RouteDefinitionLocator routeDefinitionLocator;

    public SpringDocConfig(SwaggerUiConfigProperties swaggerUiConfigProperties, RouteDefinitionLocator routeDefinitionLocator) {
        this.swaggerUiConfigProperties = swaggerUiConfigProperties;
        this.routeDefinitionLocator = routeDefinitionLocator;
    }

    @PostConstruct
    public void autoInitSwaggerUrls() {
        List<RouteDefinition> definitions = routeDefinitionLocator.getRouteDefinitions().collectList().block();
        if (definitions != null) {
            definitions.stream().filter(it -> it.getId().startsWith("ReactiveCompositeDiscoveryClient_")).forEach(routeDefinition -> {
                final String name = routeDefinition.getId().replace("ReactiveCompositeDiscoveryClient_", "").toLowerCase();
                AbstractSwaggerUiConfigProperties.SwaggerUrl swaggerUrl = new AbstractSwaggerUiConfigProperties.SwaggerUrl(
                        name, "/"+routeDefinition.getUri().toString().replace("lb://", "").toLowerCase() + "/v3/api-docs", name
                );
                Set<AbstractSwaggerUiConfigProperties.SwaggerUrl> urls = swaggerUiConfigProperties.getUrls();
                if (urls == null) {
                    urls = new LinkedHashSet<>();
                    swaggerUiConfigProperties.setUrls(urls);
                }
                urls.add(swaggerUrl);
            });
        }
    }

//    @Bean
//    @Lazy(false)
//    public List<GroupedOpenApi> apis(RouteDefinitionLocator locator) {
//        List<GroupedOpenApi> groups = new ArrayList<>();
//        List<RouteDefinition> definitions = locator.getRouteDefinitions().collectList().block();
//        assert definitions != null;
//        definitions.stream().filter(routeDefinition -> routeDefinition.getId().matches(".*-service")).forEach(routeDefinition -> {
//            String name = routeDefinition.getId().replaceAll("-service", "");
//            final GroupedOpenApi openApi = GroupedOpenApi.builder().pathsToMatch("/" + name + "/**").group(name).build();
//            groups.add(openApi);
//        });
//        return groups;
//    }

    @Bean
    public OpenAPI customOpenApi() {
        return new OpenAPI().info(new Info().title("springdoc gateway API")
                .description("springdoc gateway API")
                .version("v1.0.0")
                .contact(new Contact()
                        .name("mk")
                        .email("kuim123@gmail.com")));
    }
}
