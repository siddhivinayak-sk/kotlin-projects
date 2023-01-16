package com.sk.gateway.filter.bucket4j;

import static com.sk.gateway.filter.FilterConstants.BUCKET4J;
import static com.sk.gateway.filter.FilterConstants.BUCKET4J_ENABLED;

import java.util.List;

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(BUCKET4J)
@ConditionalOnExpression(BUCKET4J_ENABLED)
public class BucketFilterProperties {

	private List<BucketConfig> rateLimits;
	private String refillType = "";
	private String bucketType = "";

	public List<BucketConfig> getRateLimits() {
		return rateLimits;
	}

	public void setRateLimits(List<BucketConfig> rateLimits) {
		this.rateLimits = rateLimits;
	}

	public String getRefillType() {
		return refillType;
	}

	public void setRefillType(String refillType) {
		this.refillType = refillType;
	}

	public String getBucketType() {
		return bucketType;
	}

	public void setBucketType(String bucketType) {
		this.bucketType = bucketType;
	}
}
