package com.sk.gateway.resilience4j;

import static com.sk.gateway.filter.FilterConstants.RESILIENCE4J_MANUAL_ENABLED;

import java.time.Duration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;

@Configuration
@ConditionalOnProperty(RESILIENCE4J_MANUAL_ENABLED)
public class Resilience4jConfig {

	@Bean
	public RateLimiterRegistry rateLimiterRegistry() {
		RateLimiterConfig config = RateLimiterConfig.custom()
				  .limitRefreshPeriod(Duration.ofMinutes(1l))
				  .limitForPeriod(15)
				  .timeoutDuration(Duration.ofMillis(1l))
				  .build();
		return RateLimiterRegistry.of(config);
	}
	
}
