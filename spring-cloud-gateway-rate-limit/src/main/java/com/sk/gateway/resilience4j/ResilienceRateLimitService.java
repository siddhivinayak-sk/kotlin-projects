package com.sk.gateway.resilience4j;

import static com.sk.gateway.filter.FilterConstants.RESILIENCE4J_ENABLED;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import reactor.core.publisher.Mono;

@Service
@ConditionalOnProperty(RESILIENCE4J_ENABLED)
public class ResilienceRateLimitService {

	@RateLimiter(name = "basic", fallbackMethod = "globalRateLimiterfallback")
    public Mono<Void> globalRateLimiter(ServerWebExchange exchange, GatewayFilterChain chain) {
    	return chain.filter(exchange);
    }

    public Mono<Void> globalRateLimiterfallback(ServerWebExchange exchange, GatewayFilterChain chain, Throwable throwable) {
		exchange.getResponse().setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
		return exchange.getResponse().setComplete();
    }

}
