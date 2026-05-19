package com.microservices.edgeserver;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDateTime;

@SpringBootApplication
public class EdgeServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(EdgeServerApplication.class, args);
	}

	@Bean
	public RouteLocator eazyBankRouteConfig(RouteLocatorBuilder routeLocatorBuilder){
		return routeLocatorBuilder.routes()
				.route(
						r->r.path("/eazybank/accounts/**").filters(
								f -> f.rewritePath("/eazybank/accounts/(?<segment>.*)","/${segment}")
										.circuitBreaker(config -> config.setName("accountsCircuitBreaker")
												.setFallbackUri("forward:/contactSupport"))
										.addRequestHeader("X-Response-Time", LocalDateTime.now().toString())
						).uri("lb://ACCOUNTS")
				)
				.route(
						r->r.path("/eazybank/cards/**").filters(
								f->f.rewritePath("/eazybank/cards/(?<segment>.*)", "/${segment}")
//										.requestRateLimiter((c)->c.setRateLimiter(rateLimiter()))
										.addRequestHeader("X-Response-Time", LocalDateTime.now().toString())
						).uri("lb://CARDS")
				)
				.route(
						r->r.path("/eazybank/loans/**").filters(
								f->f.rewritePath("/eazybank/loans/(?<segment>.*)","/${segment}")
										.retry( retryConfig -> retryConfig
												.setMethods(HttpMethod.GET)
												.setRetries(5)
												.setBackoff(Duration.ofMillis(100), Duration.ofMillis(1000), 5, true))
										.addRequestHeader("X-Response-Time", LocalDateTime.now().toString())
						).uri("lb://LOANS")
				).build();

	}

	@Bean
	public Customizer<ReactiveResilience4JCircuitBreakerFactory> defaultCustomizer() {
		return factory -> factory.configureDefault(id -> new Resilience4JConfigBuilder(id)
				.circuitBreakerConfig(CircuitBreakerConfig.ofDefaults())
				.timeLimiterConfig(TimeLimiterConfig.custom()
						.timeoutDuration(Duration.ofSeconds(4)).build())
				.build());
	}

	@Bean
	public RedisRateLimiter rateLimiter(){
		return new RedisRateLimiter(1,1,1);
	}

	@Bean
	public KeyResolver userKeyResolver() {
		return exchange -> Mono.just(exchange.getRequest().getQueryParams().getFirst("user"))
				.defaultIfEmpty("anonymous");
	}

}
