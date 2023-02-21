package com.xmg.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 代码路由配置
 *
 * @author makui
 */
@Configuration
public class GateWayConfig {
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("accountList", predicateSpec -> predicateSpec.path("/account/list")
                        .and().path("/admin/*")
                        .uri("http://localhost:8000/account/loist")
                ).build();
    }

    /**
     * 断路器
     * @param builder
     * @return
     */
    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("circuitBreakerRoute", predicateSpec -> predicateSpec.path("/consumingServiceEndpoint")
                        .filters(f -> f.circuitBreaker(c -> c.setName("myCircuitBreaker")
                                        .setFallbackUri("forward:/inCaseOfFailureUseThis")
                                        .addStatusCode("500"))
                                .rewritePath("/consumingServiceEndpoint", "/backingServiceEndpoint"))
                        .uri("lb://backing-service:8088")
                ).build();
    }


}
