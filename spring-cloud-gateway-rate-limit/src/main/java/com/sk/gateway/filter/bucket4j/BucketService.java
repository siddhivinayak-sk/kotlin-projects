package com.sk.gateway.filter.bucket4j;

import static com.sk.gateway.filter.FilterConstants.BUCKET4J_ENABLED;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Service;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import io.github.bucket4j.local.LocalBucketBuilder;

@Service
@ConditionalOnExpression(BUCKET4J_ENABLED)
public class BucketService {

	public static final String INTERVALLY = "intervally";
	public static final String GREEDY = "greedy";
	public static final String SMOOTH = "smooth";

	public static final String NANOS = "nanos";
	public static final String MILLIS = "millis";
	public static final String SECONDS = "seconds";
	public static final String MINUTES = "minutes";
	public static final String HOURS = "hours";
	public static final String DAYS = "days";
	
	public static final String CLASSIC = "classic";
	public static final String SIMPLE = "simple";
	
	BucketFilterProperties bucketFilterProperties;
	
	public BucketService(BucketFilterProperties bucketFilterProperties) {
		this.bucketFilterProperties = bucketFilterProperties;
	}
	
	public Bucket newBucket(Long availableTokens) {
		LocalBucketBuilder builder = Bucket.builder();
		for(BucketConfig limit:bucketFilterProperties.getRateLimits()) {
			builder.addLimit(bandwidth(bucketFilterProperties.getBucketType(), limit.getCapacity(), bucketFilterProperties.getRefillType(), limit.getTime(), limit.getUnit(), availableTokens));
		}
		Bucket bucket = builder.build();
		long tokens = bucket.getAvailableTokens();
		return bucket;
	}

	public Bucket newBucket() {
		LocalBucketBuilder builder = Bucket.builder();
		for(BucketConfig limit:bucketFilterProperties.getRateLimits()) {
			builder.addLimit(bandwidth(bucketFilterProperties.getBucketType(), limit.getCapacity(), bucketFilterProperties.getRefillType(), limit.getTime(), limit.getUnit()));
		}
        return builder.build();
	}
	
	private Bandwidth bandwidth(String bandwidthType, Integer capacity, String refillType, Integer time, String unit) {
		switch(bandwidthType) {
		case SIMPLE:
			Duration duration = of(time, unit);
			return Bandwidth.simple(capacity, duration);
		default:
			Refill refill = refill(refillType, capacity, time, unit);
			return Bandwidth.classic(capacity, refill);
		}
	}

	private Bandwidth bandwidth(String bandwidthType, Integer capacity, String refillType, Integer time, String unit, Long initialToken) {
		Bandwidth bandwidth;
		switch(bandwidthType) {
		case SIMPLE:
			Duration duration = of(time, unit);
			bandwidth = Bandwidth.simple(capacity, duration);
			break;
		default:
			Refill refill = refill(refillType, capacity, time, unit);
			bandwidth = Bandwidth.classic(capacity, refill);
			break;
		}
		bandwidth.withInitialTokens(initialToken);
		return bandwidth;
	}
	
	private Refill refill(String refillType, Integer capacity, Integer time, String unit) {
		Duration duration = of(time, unit);
		switch(refillType) {
			case GREEDY: return Refill.greedy(capacity, duration);
			case SMOOTH: return Refill.smooth(capacity, duration);
			default: return Refill.intervally(capacity, duration);
		}
	}
	
	private Duration of(Integer time, String unit) {
		switch(unit) {
			case NANOS: return Duration.ofNanos(time);
			case MILLIS: return Duration.ofMillis(time);
			case SECONDS: return Duration.ofSeconds(time);
			case MINUTES: return Duration.ofMinutes(time);
			case HOURS: return Duration.ofHours(time);
			case DAYS: return Duration.ofDays(time);
			default: return Duration.ofSeconds(time);
		}
	}
}
