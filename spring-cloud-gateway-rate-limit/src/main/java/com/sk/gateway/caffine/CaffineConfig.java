package com.sk.gateway.caffine;

import static com.sk.gateway.filter.FilterConstants.BUCKET4J_CAFFINE_ENABLED;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.sk.gateway.filter.bucket4j.BucketService;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;


@Configuration
@EnableCaching
@ConditionalOnProperty(BUCKET4J_CAFFINE_ENABLED)
public class CaffineConfig {

	@Bean
	public Cache<String, Bucket> cache() {
		Cache<String, Bucket> cache = Caffeine.newBuilder()
				  .expireAfterAccess(5, TimeUnit.MINUTES)
				  .maximumSize(Integer.MAX_VALUE)
				  .build();
		return cache;
	}
	
	@Bean
	public LoadingCache<String, Bucket> loadingCache(BucketService bucketService) {
		LoadingCache<String, Bucket> cache = Caffeine.newBuilder()
				  .expireAfterAccess(5, TimeUnit.MINUTES)
				  .maximumSize(Integer.MAX_VALUE)
				  .build(key -> {
					  return bucketService.newBucket();
				  });
		return cache;
	}
}
