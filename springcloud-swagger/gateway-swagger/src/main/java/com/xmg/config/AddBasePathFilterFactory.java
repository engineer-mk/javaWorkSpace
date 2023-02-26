package com.xmg.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Setter;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.factory.rewrite.ModifyResponseBodyGatewayFilterFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author makui
 * @created 2023/2/26
 **/
@Component
public class AddBasePathFilterFactory extends AbstractGatewayFilterFactory<AddBasePathFilterFactory.Config> {


    private final ModifyResponseBodyGatewayFilterFactory modifyResponseBodyGatewayFilterFactory;

    public AddBasePathFilterFactory(ModifyResponseBodyGatewayFilterFactory modifyResponseBodyGatewayFilterFactory) {
        super(Config.class);
        this.modifyResponseBodyGatewayFilterFactory = modifyResponseBodyGatewayFilterFactory;
    }

    @Override
    public GatewayFilter apply(Config config) {
        ModifyResponseBodyGatewayFilterFactory.Config cf = new ModifyResponseBodyGatewayFilterFactory.Config()
                .setRewriteFunction(JsonNode.class, JsonNode.class,
                        (e, jsonNode) -> Mono.justOrEmpty(addBasePath(e, jsonNode)));
        return modifyResponseBodyGatewayFilterFactory.apply(cf);
    }

    @Override
    public String name() {
        return "AddBasePath";
    }

    @Setter
    public static class Config {
    }

    private JsonNode addBasePath(ServerWebExchange exchange, JsonNode jsonNode) {
        if (jsonNode.isObject()) {
            ObjectNode node = (ObjectNode) jsonNode;
            String basePath = exchange.getRequest().getPath().subPath(4).value();
            node.put("basePath", basePath);
            return node;
        }
        return jsonNode;
    }
}
