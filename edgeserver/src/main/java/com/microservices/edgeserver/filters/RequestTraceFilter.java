package com.microservices.edgeserver.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Order(1)
@Component
public class RequestTraceFilter implements GlobalFilter {

    private static final Logger logger = LoggerFactory.getLogger(RequestTraceFilter.class);
    String correlationId;
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerWebExchange mutatedExchange = exchange;
        HttpHeaders httpHeaders = exchange.getRequest().getHeaders();
        if(httpHeaders.get("eazybank-correlation-id") == null || httpHeaders.get("eazybank-correlation-id").isEmpty()){
            correlationId = UUID.randomUUID().toString();
            mutatedExchange = exchange.mutate().request(exchange.getRequest().mutate().header("eazybank-correlation-id",correlationId).build()).build();
        }
        logger.debug("eazyBank-correlation-id generated in RequestTraceFilter : {}",correlationId);
        return chain.filter(mutatedExchange);
    }

}
