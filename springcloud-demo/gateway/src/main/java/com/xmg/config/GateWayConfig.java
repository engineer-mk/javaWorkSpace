package com.xmg.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.ZonedDateTime;

//代码路由配置
@Configuration
public class GateWayConfig {
    //@Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder routeLocatorBuilder){
        final RouteLocatorBuilder.Builder builder = routeLocatorBuilder.routes()
                .route("accountList", predicateSpec -> predicateSpec.path("/account/list")
                        .uri("http://localhost:8000/account/loist"));
        return builder.build();
    }
    public static void main(String[] args) {
        ZonedDateTime now = ZonedDateTime.now();
        System.out.println(now.toString());
    }
 }
