package com.sk.gateway.filter;

import static com.sk.gateway.filter.FilterConstants.BUCKET4J_CAFFINE_ENABLED;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.sk.gateway.filter.bucket4j.BucketService;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import reactor.core.publisher.Mono;

@Component
@ConditionalOnProperty(BUCKET4J_CAFFINE_ENABLED)
public class Bucket4jRateLimitFilterWithCaffine implements GlobalFilter {

	@Autowired
	Cache<String, Bucket> cache;
	
	@Autowired
	LoadingCache<String, Bucket> loadingCache;
	
	@Autowired
	BucketService bucketService;

    private Bucket resolveBucket(String key) {
    	Bucket bucket = cache.getIfPresent(key);
    	if(null != bucket) {
    		return bucket;
    	} else {
    		bucket = bucketService.newBucket();
    		cache.put(key, bucket);
    		return bucket;
    	}
    }

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
		//Bucket bucket = resolveBucket(route.getId());
		Bucket bucket = loadingCache.get(route.getId());
		//Bucket bucket = resolveBucketEhCache(route.getId());
		if(bucket.tryConsume(1)) {
			return chain.filter(exchange);
		} else {
			exchange.getResponse().setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
			return exchange.getResponse().setComplete();
		}
		
		
	}
	
	

}
