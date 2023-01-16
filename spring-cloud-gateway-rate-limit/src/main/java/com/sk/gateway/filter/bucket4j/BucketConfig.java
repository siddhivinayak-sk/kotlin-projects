package com.sk.gateway.filter.bucket4j;

import java.io.Serializable;

public class BucketConfig implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer capacity;
	private Integer time;
	private String unit;

	public Integer getCapacity() {
		return capacity;
	}
	public void setCapacity(Integer capacity) {
		this.capacity = capacity;
	}
	public Integer getTime() {
		return time;
	}
	public void setTime(Integer time) {
		this.time = time;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
}
