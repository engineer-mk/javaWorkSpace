package com.xmg.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.AbstractSwaggerUiConfigProperties;
import org.springdoc.core.SwaggerUiConfigProperties;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * springDoc 配置
 *
 * @author makui
 * @created 2023/2/24
 **/
@Slf4j
@Configuration
@RequiredArgsConstructor
public class SpringDocConfig {
    private final SwaggerUiConfigProperties swaggerUiConfigProperties;
    private final RouteDefinitionLocator routeDefinitionLocator;
    private final SpringDocRouteLocator springDocRouteLocator;
    private final DiscoveryClient discoveryClient;
    private final ApplicationProperties applicationProperties;

    private static final ScheduledExecutorService service = Executors.newScheduledThreadPool(1);

    @PostConstruct
    public void refSwaggerUrls() {
        service.scheduleAtFixedRate(this::doRefSwaggerUrls, 5, 5, TimeUnit.SECONDS);
    }

    private void doRefSwaggerUrls() {
        final List<String> docServices = discoveryClient.getServices().stream()
                .filter(applicationProperties.getDocServices()::contains)
                .collect(Collectors.toList());
        if (docServices.isEmpty()) {
            return;
        }
        final Set<AbstractSwaggerUiConfigProperties.SwaggerUrl> swaggerUrls = docServices.stream()
                .map(it -> {
                    final String url = "/" + it + "/v3/api-docs";
                    return new AbstractSwaggerUiConfigProperties.SwaggerUrl(it, url, it);
                })
                .collect(Collectors.toSet());
        if (Objects.equals(swaggerUiConfigProperties.getUrls(), swaggerUrls)) {
            return;
        }
        swaggerUiConfigProperties.setUrls(swaggerUrls);
        springDocRouteLocator.buildRoutes(docServices);

    }

    public void createSwaggerUrls() {
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
                }).subscribe();
    }

}
