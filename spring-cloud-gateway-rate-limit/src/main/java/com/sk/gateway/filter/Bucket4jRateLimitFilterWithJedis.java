package com.sk.gateway.filter;

import static com.sk.gateway.filter.FilterConstants.BUCKET4J_JEDIS_ENABLED;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.SerializationUtils;
import org.springframework.web.server.ServerWebExchange;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import reactor.core.publisher.Mono;

@Component
@ConditionalOnProperty(BUCKET4J_JEDIS_ENABLED)
public class Bucket4jRateLimitFilterWithJedis implements GlobalFilter {

	@Autowired
	RedisTemplate<String, Bucket> redisTemplate;

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		Route route = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
		Bucket bucket = resolveBucketRedis(route.getId());
		if(bucket.tryConsume(1)) {
			return chain.filter(exchange);
		} else {
			exchange.getResponse().setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
			return exchange.getResponse().setComplete();
		}
	}

    private Bucket resolveBucketRedis(String key) {
    	return redisTemplate.execute(new RedisCallback<Bucket>() {
			@Override
			public Bucket doInRedis(RedisConnection connection) throws DataAccessException {
				byte[] object = connection.get(key.getBytes());
				if(null != object) {
					return (Bucket)SerializationUtils.deserialize(object);
				} else {
					Bucket bucket = newBucket(key);
					connection.set(key.getBytes(), SerializationUtils.serialize(bucket));
					return bucket;
				}
			}
    	});
    }
    
    private Bucket newBucket(String key) {
    	Refill refill = Refill.intervally(15, Duration.ofMinutes(1l));
    	Bandwidth limit = Bandwidth.classic(15, refill);
        return Bucket.builder()
            .addLimit(limit)
            .build();
    }	
    
}
