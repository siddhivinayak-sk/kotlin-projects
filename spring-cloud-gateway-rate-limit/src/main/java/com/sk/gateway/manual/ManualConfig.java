package com.sk.gateway.manual;

import static com.sk.gateway.filter.FilterConstants.BUCKET4J_MANUAL_ENABLED;

import java.util.concurrent.TimeUnit;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.sk.gateway.filter.bucket4j.BucketService;


@Configuration
@EnableCaching
@ConditionalOnProperty(BUCKET4J_MANUAL_ENABLED)
public class ManualConfig {

	@Bean
	public Cache<String, Long> loadingCache(BucketService bucketService) {
		Cache<String, Long> cache = Caffeine.newBuilder()
				  .expireAfterAccess(5, TimeUnit.MINUTES)
				  .maximumSize(Integer.MAX_VALUE)
				  .build();
		return cache;
	}
}
