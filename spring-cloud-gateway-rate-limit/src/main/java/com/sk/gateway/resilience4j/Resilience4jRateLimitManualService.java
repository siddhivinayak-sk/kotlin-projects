package com.sk.gateway.resilience4j;

import static com.sk.gateway.filter.FilterConstants.RESILIENCE4J_MANUAL_ENABLED;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import io.vavr.CheckedRunnable;
import io.vavr.control.Try;
import reactor.core.publisher.Mono;

@Service
@ConditionalOnProperty(RESILIENCE4J_MANUAL_ENABLED)
public class Resilience4jRateLimitManualService {
	
	@Autowired
	RateLimiterRegistry rateLimiterRegistry;
	
    public Mono<Void> globalRateLimiter(ServerWebExchange exchange, GatewayFilterChain chain) {
		Route route = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
    	RateLimiter rateLimiter = rateLimiterRegistry.rateLimiter(route.getId());
    	CheckedRunnable restrictedCall = RateLimiter
    			.decorateCheckedRunnable(rateLimiter, () -> chain.filter(exchange));
    	
    	return Mono.just(
    	Try.run(restrictedCall)
        .onFailure((throwable) -> {
        	globalRateLimiterfallback(exchange, chain, throwable);
        }).get());
    }

    public Mono<Void> globalRateLimiterfallback(ServerWebExchange exchange, GatewayFilterChain chain, Throwable throwable) {
		exchange.getResponse().setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
		return exchange.getResponse().setComplete();
    }
	

}
