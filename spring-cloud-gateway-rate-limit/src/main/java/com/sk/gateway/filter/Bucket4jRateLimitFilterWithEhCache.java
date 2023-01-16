package com.sk.gateway.filter;

import static com.sk.gateway.filter.FilterConstants.BUCKET4J_EHCACHE_ENABLED;

import java.time.Duration;
import java.util.concurrent.Callable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.sk.gateway.filter.bucket4j.BucketService;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import reactor.core.publisher.Mono;

@Component
@ConditionalOnProperty(BUCKET4J_EHCACHE_ENABLED)
public class Bucket4jRateLimitFilterWithEhCache implements GlobalFilter {

	@Autowired
	EhCacheCacheManager ehCacheManager;
	org.springframework.cache.Cache ehCache;
	
	@Autowired
	BucketService bucketService;

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		/**
		 * Bucket4j is rate limiting library specially designed to work as try/block/reject using leaky bucket
		 * algorithm. It provides effective API rate limiting with Spring Boot but its integrated libs are not
		 * working with spring cloud gateway.
		 * To work with spring cloud gateway, wrote custom filter which uses bucket4j-core and it is working.
		 * Limitation, Bucket4j works with JCache (Object caching) and compatible with Caffine, EhCache, Hazelcast.
		 * Tested Caffine, EhCache and working fine (95% success rate on high load) but cache replication is not 
		 * feasible with Caffine, EhCache. Hazelcast can be used but need Hazelcast cluster in this case. 
		 */

		Route route = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
		Bucket bucket = resolveBucketEhCache(route.getId());
		if(bucket.tryConsume(1)) {
			return chain.filter(exchange);
		} else {
			exchange.getResponse().setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
			return exchange.getResponse().setComplete();
		}
	}

	private Bucket resolveBucketEhCache(String key) {
		if(null == ehCache) {
			ehCache = ehCacheManager.getCache("tokens");
		}
		return ehCache.get(key, new Callable<Bucket>() {

			@Override
			public Bucket call() throws Exception {
				return bucketService.newBucket();
			}});
	}
	
    private Bucket newBucket(String key) {
    	Refill refill = Refill.intervally(15, Duration.ofMinutes(1l));
    	Bandwidth limit = Bandwidth.classic(15, refill);
        return Bucket.builder()
            .addLimit(limit)
            .build();
    }	
	
}
