package com.sk.gateway.filter;

import static com.sk.gateway.filter.FilterConstants.JEDIS_CUSTOM_ENABLED;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.sk.gateway.jedis.CustomJedisService;

import reactor.core.publisher.Mono;

@Component
@ConditionalOnProperty(JEDIS_CUSTOM_ENABLED)
public class CustomJedisRateLimitFilter implements GlobalFilter {

	@Autowired
	CustomJedisService customJedisService;

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		Route route = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
		return customJedisService.executeRedisMulti(route.getId())
		.flatMap(args -> {
			//System.out.println("Allowed: " + args.isAllowed() + "   Remaining Token: " + args.remainingToken);
			if(args.isAllowed())
				return chain.filter(exchange);
			else 
				return requestRejected(exchange);
		});
	}
	
	private Mono<Void> requestRejected(ServerWebExchange exchange) {
		exchange.getResponse().setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
		return exchange.getResponse().setComplete();
	}
}
