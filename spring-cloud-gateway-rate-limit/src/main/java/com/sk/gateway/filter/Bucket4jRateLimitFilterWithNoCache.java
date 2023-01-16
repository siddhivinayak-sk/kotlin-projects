package com.sk.gateway.filter;

import static com.sk.gateway.filter.FilterConstants.BUCKET4J_NOCACHE_ENABLED;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.sk.gateway.filter.bucket4j.BucketService;

import io.github.bucket4j.Bucket;
import reactor.core.publisher.Mono;

@Component
@ConditionalOnProperty(BUCKET4J_NOCACHE_ENABLED)
public class Bucket4jRateLimitFilterWithNoCache implements GlobalFilter {

	private final Map<String, CustomBucket> cache = new ConcurrentHashMap<>();
	
	@Autowired
	BucketService bucketService;

    private CustomBucket resolveBucket(String key) {
    	CustomBucket retVal = cache.computeIfAbsent(key, (k) -> {
    		Bucket bucket = bucketService.newBucket();
    		CustomBucket cb = new CustomBucket();
    		cb.setBucket(bucket);
    		cb.setInstant(Instant.now());
    		return cb;
    	});
    	retVal.setInstant(Instant.now());
    	return retVal;
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
		Bucket bucket = resolveBucket(route.getId()).getBucket();
		if(bucket.tryConsume(1)) {
			return chain.filter(exchange);
		} else {
			exchange.getResponse().setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
			return exchange.getResponse().setComplete();
		}
	}
 	
 	public Bucket4jRateLimitFilterWithNoCache() {
 		ScheduledExecutorService executor = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());
 		executor.scheduleWithFixedDelay(() -> {
 			Instant expiry = Instant.now().minus(300, ChronoUnit.SECONDS);
 			cache.forEach((key, value) -> {
 				if(value.getInstant().isBefore(expiry)) {
 					cache.remove(key);
 				}
 			}); 			
 		}, 60, 300, TimeUnit.SECONDS);
 	}
 	
 	static class CustomBucket {
 		private Bucket bucket;
 		private Instant instant;

 		public Bucket getBucket() {
			return bucket;
		}
		public void setBucket(Bucket bucket) {
			this.bucket = bucket;
		}
		public Instant getInstant() {
			return instant;
		}
		public void setInstant(Instant instant) {
			this.instant = instant;
		}
 	}
}
