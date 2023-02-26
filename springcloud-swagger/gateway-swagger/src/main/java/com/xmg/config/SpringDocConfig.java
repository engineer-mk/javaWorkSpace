package com.xmg.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.AbstractSwaggerUiConfigProperties;
import org.springdoc.core.SwaggerUiConfigProperties;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author makui
 * @created 2023/2/24
 **/
@Slf4j
@Configuration
@RequiredArgsConstructor
public class SpringDocConfig {
    protected final SwaggerUiConfigProperties swaggerUiConfigProperties;
    protected final RouteDefinitionLocator routeDefinitionLocator;


//    @PostConstruct
//    public void autoInitSwaggerUrls() {
//        List<RouteDefinition> definitions = routeDefinitionLocator.getRouteDefinitions().collectList().block();
//        if (definitions != null) {
//            definitions.stream().filter(it -> it.getId().startsWith("ReactiveCompositeDiscoveryClient_")).forEach(routeDefinition -> {
//                final String name = routeDefinition.getId().replace("ReactiveCompositeDiscoveryClient_", "").toLowerCase();
//                AbstractSwaggerUiConfigProperties.SwaggerUrl swaggerUrl = new AbstractSwaggerUiConfigProperties.SwaggerUrl(
//                        name, "/" + routeDefinition.getUri().toString().replace("lb://", "").toLowerCase() + "/v3/api-docs", name
//                );
//                Set<AbstractSwaggerUiConfigProperties.SwaggerUrl> urls = swaggerUiConfigProperties.getUrls();
//                if (urls == null) {
//                    urls = new LinkedHashSet<>();
//                    swaggerUiConfigProperties.setUrls(urls);
//                }
//                urls.add(swaggerUrl);
//            });
//        }
//    }

    @PostConstruct
    public void autoInitSwaggerUrls() {
        routeDefinitionLocator.getRouteDefinitions()
                .filter(it -> it.getId().startsWith("ReactiveCompositeDiscoveryClient_"))
                .flatMap(it -> {
                    final String serviceName = it.getId().replace("ReactiveCompositeDiscoveryClient_", "").toLowerCase();
                    final String url = "/" + it.getUri().toString().replace("lb://", "").toLowerCase() + "/v3/api-docs";
                    final AbstractSwaggerUiConfigProperties.SwaggerUrl swaggerUrl = new AbstractSwaggerUiConfigProperties.SwaggerUrl(serviceName, url, serviceName);
                    final Set<AbstractSwaggerUiConfigProperties.SwaggerUrl> urls = swaggerUiConfigProperties.getUrls() == null ? new LinkedHashSet<>() : swaggerUiConfigProperties.getUrls();
                    urls.add(swaggerUrl);
                    return Mono.just(urls);
                }).map(it -> {
                    swaggerUiConfigProperties.setUrls(it);
                    return it;
                })
                .subscribe(it->{
                    log.info("SwaggerUrl size：" + it.size());
                });
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

}
