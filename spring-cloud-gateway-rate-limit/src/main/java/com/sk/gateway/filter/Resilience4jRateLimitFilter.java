package com.sk.gateway.filter;

import static com.sk.gateway.filter.FilterConstants.RESILIENCE4J_ENABLED;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.sk.gateway.resilience4j.ResilienceRateLimitService;

import reactor.core.publisher.Mono;

@Component
@ConditionalOnProperty(RESILIENCE4J_ENABLED)
public class Resilience4jRateLimitFilter implements GlobalFilter {

	@Autowired
	ResilienceRateLimitService resilienceRateLimitService;

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		/**
		 * Resilience4j works on method level rate limiting with retry and
		 * fallback mechanims and very useful in Circuit Breaker scenario.
		 * Although, limit based circuit break can be utilized to rate limit
		 * on Filter but Resilience4j is not working with Reactor.
		*/
		
		return resilienceRateLimitService.globalRateLimiter(exchange, chain);
	}

}
