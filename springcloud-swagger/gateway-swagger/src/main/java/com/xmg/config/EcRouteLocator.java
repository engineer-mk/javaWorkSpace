package com.xmg.config;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.Buildable;
import org.springframework.cloud.gateway.route.builder.PredicateSpec;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * springDoc 路由
 *
 * @author makui
 * @created 2023/2/24
 **/
@Component
@RequiredArgsConstructor
public class EcRouteLocator implements RouteLocator {
    private final RouteLocatorBuilder builder;
    private static final String API_ROUTE_PREFIX = "api-docs-route-";
    private Flux<Route> route = Flux.empty();

    @Override
    public Flux<Route> getRoutes() {
        return route;
    }


    @PostConstruct
    public void buildRoutes() {
        final RouteLocatorBuilder.Builder builder = this.builder.routes();
        builder.route(API_ROUTE_PREFIX + "EC", EcRouteLocator::buildable);
        this.route = builder.build().getRoutes();
    }

    /**
     * api-doc 修改ResponseBody 为接口添加服务名称前缀
     */
    private static Buildable<Route> buildable(PredicateSpec r) {
        return r.path("/qr/resources/image/info")
                .filters(f -> f.modifyResponseBody(String.class, String.class,
                        (exchange, s) -> {
                            if (StringUtils.isBlank(s)) {
                                return Mono.just(s);
                            }
                            final JSONObject jsonObject = JSON.parseObject(s);
                            if (!jsonObject.containsKey("data")) {
                                return Mono.just(s);
                            }
                            final JSONObject result = new JSONObject();
                            final JSONObject data = JSON.parseObject(JSON.toJSONString(JSONArray.parse(JSON.toJSONString(jsonObject.get("data"))).get(0)));
                            for (final Map.Entry<String, Object> entry : data.entrySet()) {
                                String key = entry.getKey();
                                final String value = entry.getValue().toString();
                                if (key.equals("image_url")) {
                                    if (value.contains("!1500.png")) {
                                        final String replace = value.replace("!1500.png", "");
                                        result.put(key, replace);
                                    } else {
                                        result.put(key, value);
                                    }
                                } else {
                                    result.put(key, value);
                                }
                            }
                            jsonObject.put("data", result);
                            return Mono.just(getResult());
                        }))
                .uri("http://open-ec-service.qrkjdiy.com");
    }

    public static String getResult() {
        return "{\n" +
                "  \"code\": 1,\n" +
                "  \"message\": \"success\",\n" +
                "  \"data\": [\n" +
                "    {\n" +
                "      \"global_id\": \"6663166599119264910001\",\n" +
                "      \"version\": null,\n" +
                "      \"can_update\": true,\n" +
                "      \"can_delete\": true,\n" +
                "      \"can_recover\": true,\n" +
                "      \"config_global\": \"QRC\",\n" +
                "      \"name\": \"1581905038219935744\",\n" +
                "      \"folder\": \"\",\n" +
                "      \"type\": \".png\",\n" +
                "      \"alias_name\": null,\n" +
                "      \"original_alias_name\": \"\",\n" +
                "      \"note\": \"\",\n" +
                "      \"url\": \"https://qr-mall-cloud-prod.oss-cn-guangzhou.aliyuncs.com/material/prod/2049/1665990575000-hTiZj3wmKXhfymTn.png\",\n" +
                "      \"size\": \"484.044KB\",\n" +
                "      \"original_size\": \"2942937\",\n" +
                "      \"duration\": \"\",\n" +
                "      \"width\": \"5000\",\n" +
                "      \"height\": \"5000\",\n" +
                "      \"config_object\": null,\n" +
                "      \"image_url\": \"https://qr-mall-cloud-prod.oss-cn-guangzhou.aliyuncs.com/material/prod/2049/1665990575000-hTiZj3wmKXhfymTn.png\",\n" +
                "      \"folder_global\": \"\",\n" +
                "      \"custom_code\": \"\",\n" +
                "      \"custom_name\": \"\",\n" +
                "      \"help_info\": null,\n" +
                "      \"ind_code\": \"\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";
    }

}
