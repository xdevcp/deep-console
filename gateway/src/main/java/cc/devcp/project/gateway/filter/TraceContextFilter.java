package cc.devcp.project.gateway.filter;

import brave.Tracer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @version 1.0.0
 * @author: lzy
 * @date: 2019/12/17 9:35
 */
@Component
public class TraceContextFilter implements GlobalFilter, Ordered {

    @Autowired
    Tracer tracer;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String traceId = tracer.currentSpan().context().traceIdString();
        exchange.getResponse().getHeaders().set("X-B3-TraceId", traceId);
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
