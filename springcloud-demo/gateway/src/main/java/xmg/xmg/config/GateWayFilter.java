package xmg.xmg.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

//自定义全局过滤器
@Slf4j
@Configuration
public class GateWayFilter {
    @Bean
    public GlobalFilter customFilter() {
        return new CustomGlobalFilter();
    }

    public static class CustomGlobalFilter implements GlobalFilter, Ordered {

        @Override
        public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
            final ServerHttpRequest request = exchange.getRequest();
            if (request.getHeaders().containsKey("a")) {
                return exchange.getResponse().setComplete();
            }
            return chain.filter(exchange);
        }

        //顺序，数字越小优先级越高
        @Override
        public int getOrder() {
            return -1;
        }
    }
}
