package com.microservices.edgeserver.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class ResponseTraceFilter implements GlobalFilter {

    private static final Logger logger = LoggerFactory.getLogger(ResponseTraceFilter.class);
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        return chain.filter(exchange).then(
                Mono.fromRunnable(
                        ()->{
                            System.out.println(exchange.getRequest().getHeaders().get("eazybank-correlation-id"));
                            String correlationId = exchange.getRequest().getHeaders().get("eazybank-correlation-id").stream().findFirst().get();
                            if(!exchange.getResponse().getHeaders().containsHeader("eazybank-correlation-id")){
                                logger.debug("Updated the correlation id to the outbound headers: {}",correlationId);
                                exchange.getResponse().getHeaders()
                                        .add("eazybank-correlation-id",correlationId);
                            }

                        }
                )
        );
    }
}
