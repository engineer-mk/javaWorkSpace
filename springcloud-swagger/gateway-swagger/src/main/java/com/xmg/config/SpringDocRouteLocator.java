package com.xmg.config;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.gateway.config.GatewayProperties;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.Buildable;
import org.springframework.cloud.gateway.route.builder.PredicateSpec;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * springDoc 路由
 *
 * @author makui
 * @created 2023/2/24
 **/
@Component
@RequiredArgsConstructor
public class SpringDocRouteLocator implements RouteLocator {
    private final RouteLocatorBuilder builder;
    private final ApplicationProperties applicationProperties;
    private static final String PATHS = "paths";
    private static final String API_ROUTE_PREFIX = "api-docs-route-";
    private Flux<Route> route = Flux.empty();

    @Override
    public Flux<Route> getRoutes() {
        return route;
    }

    /**
     * 配置完成后，调用本方法构建路由和刷新路由表
     * <a href="http://127.0.0.1:8003/actuator/gateway/routes">查看所有路由信息</a>
     */
    public void buildRoutes(List<String> serviceNames) {
        final Set<String> docServices = applicationProperties.getDocServices();
        final List<String> refNames = serviceNames.stream()
                .filter(docServices::contains)
                .collect(Collectors.toList());
        if (!refNames.isEmpty()) {
            final RouteLocatorBuilder.Builder builder = this.builder.routes();
            for (final String refName : refNames) {
                builder.route(API_ROUTE_PREFIX + refName, r -> buildable(refName, r));
            }
            this.route = builder.build().getRoutes();
        }
    }

    /**
     * api-doc 修改ResponseBody 为接口添加服务名称前缀
     */
    private static Buildable<Route> buildable(String serverName, PredicateSpec r) {
        return r.path("/" + serverName + "/v3/api-docs")
                .filters(f -> f.modifyResponseBody(String.class, String.class,
                        (exchange, s) -> {
                            if (StringUtils.isBlank(s)) {
                                return Mono.just(s);
                            }
                            final JSONObject jsonObject = JSON.parseObject(s);
                            if (!jsonObject.containsKey(PATHS)) {
                                return Mono.just(s);
                            }
                            final JSONObject result = new JSONObject();
                            final JSONObject paths = JSON.parseObject(JSON.toJSONString(jsonObject.get(PATHS)));
                            for (final Map.Entry<String, Object> entry : paths.entrySet()) {
                                String key = "/" + serverName + entry.getKey();
                                result.put(key, entry.getValue());
                            }
                            jsonObject.put(PATHS, result);
                            return Mono.just(jsonObject.toJSONString());
                        }).stripPrefix(1))
                .uri("lb://" + serverName);
    }

}
