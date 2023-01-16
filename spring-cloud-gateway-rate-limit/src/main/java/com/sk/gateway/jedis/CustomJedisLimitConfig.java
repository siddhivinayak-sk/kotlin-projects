package com.sk.gateway.jedis;

import java.time.Duration;

public class CustomJedisLimitConfig {
	private Long requestedToken;
	private Long capacity;
	private Long fillType;  // 0 - intervally, 1 - greedy
	private Long time;
	private String unit;

	public Long getRequestedToken() {
		return requestedToken;
	}
	public void setRequestedToken(Long requestedToken) {
		this.requestedToken = requestedToken;
	}
	public Long getCapacity() {
		return capacity;
	}
	public void setCapacity(Long capacity) {
		this.capacity = capacity;
	}
	public Long getFillType() {
		return fillType;
	}
	public void setFillType(Long fillType) {
		this.fillType = fillType;
	}
	public Long getTime() {
		return time;
	}
	public void setTime(Long time) {
		this.time = time;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public Long getFillTime() {
		return of(time, unit).getSeconds();
	}
	
	public static final String NANOS = "nanos";
	public static final String MILLIS = "millis";
	public static final String SECONDS = "seconds";
	public static final String MINUTES = "minutes";
	public static final String HOURS = "hours";
	public static final String DAYS = "days";

	private Duration of(Long time, String unit) {
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
