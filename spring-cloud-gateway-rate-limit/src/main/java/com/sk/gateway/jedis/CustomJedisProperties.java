package com.sk.gateway.jedis;

import static com.sk.gateway.filter.FilterConstants.JEDIS_CUSTOM;
import static com.sk.gateway.filter.FilterConstants.JEDIS_CUSTOM_ENABLED;

import java.util.List;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(JEDIS_CUSTOM_ENABLED)
@ConfigurationProperties(JEDIS_CUSTOM)
public class CustomJedisProperties {

	private List<CustomJedisLimitConfig> rateLimits;

	public List<CustomJedisLimitConfig> getRateLimits() {
		return rateLimits;
	}

	public void setRateLimits(List<CustomJedisLimitConfig> rateLimits) {
		this.rateLimits = rateLimits;
	}
}
