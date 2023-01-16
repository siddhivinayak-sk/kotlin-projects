package com.sk.gateway.filter;

import static com.sk.gateway.filter.FilterConstants.RESILIENCE4J_MANUAL_ENABLED;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.sk.gateway.resilience4j.Resilience4jRateLimitManualService;

import reactor.core.publisher.Mono;

@Component
@ConditionalOnProperty(RESILIENCE4J_MANUAL_ENABLED)
public class Resilience4jRateLimitFilterWithManual implements GlobalFilter {

	@Autowired
	Resilience4jRateLimitManualService service;

 	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		return service.globalRateLimiter(exchange, chain);
		
	}
}
