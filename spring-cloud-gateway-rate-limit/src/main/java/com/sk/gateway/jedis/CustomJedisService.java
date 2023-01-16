package com.sk.gateway.jedis;

import static com.sk.gateway.filter.FilterConstants.JEDIS_CUSTOM_ENABLED;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@ConditionalOnProperty(JEDIS_CUSTOM_ENABLED)
public class CustomJedisService {

	@Autowired
	CustomJedisProperties customJedisProperties;
	
	@Autowired
	ReactiveStringRedisTemplate redisTemplate;
	
	@Autowired
	DefaultRedisScript<String> redisScript;

	public Mono<JedisExecuteArgs> executeRedisMulti(String key) {
		AtomicInteger counter = new AtomicInteger();
		return
		Flux.fromIterable(customJedisProperties.getRateLimits())
		.flatMap(config -> executeRedis(config, counter.getAndIncrement() + "." + key))
		.reduce((JedisExecuteArgs r1, JedisExecuteArgs r2) -> {
			r1.setAllowed(r1.isAllowed() && r2.isAllowed());
			System.out.println("R1: " + r1.getRemainingToken() + "       R2: " + r2.getRemainingToken());
			return r1;
		});
	}
	
	public Mono<JedisExecuteArgs> executeRedis(String key) {
		return executeRedis(customJedisProperties.getRateLimits().get(0), key);
	}
	
	private Mono<JedisExecuteArgs> executeRedis(CustomJedisLimitConfig config, String key) {
		JedisExecuteArgs retVal = new JedisExecuteArgs();
		List<String> keys = getKeys(key);
		List<String> args = Arrays
				.asList(config.getRequestedToken(), config.getCapacity(), config.getFillType(), config.getFillTime())
				.stream()
				.map(arg -> arg.toString())
				.collect(Collectors.toList());
		
		return redisTemplate.execute(redisScript, keys, args)
		.reduce(new ArrayList<String>(), (r1, r2) -> {
			r1.add(r2);
			return r1;
		}).map(list -> {
			//System.out.println(list);
			if(!list.isEmpty() && null != list.get(0)) {
				String[] rt = list.get(0).split("-");
				retVal.setAllowed(rt[0].equals("1"));
				retVal.setRemainingToken(rt[1]);
			}
			return retVal;
		});
	}
	
	private List<String> getKeys(String key) {
		return Arrays.asList(
				"mylimiter." + key + ".token", 
				"mylimiter." + key + ".timestamp"
				);
	}
	
	public static class JedisExecuteArgs {
		String remainingToken;
		boolean allowed;
		String key;
		long capacity;
		long time;
		String unit;
		
		public String getRemainingToken() {
			return remainingToken;
		}
		public void setRemainingToken(String remainingToken) {
			this.remainingToken = remainingToken;
		}
		public boolean isAllowed() {
			return allowed;
		}
		public void setAllowed(boolean allowed) {
			this.allowed = allowed;
		}
		public String getKey() {
			return key;
		}
		public void setKey(String key) {
			this.key = key;
		}
		public long getCapacity() {
			return capacity;
		}
		public void setCapacity(long capacity) {
			this.capacity = capacity;
		}
		public long getTime() {
			return time;
		}
		public void setTime(long time) {
			this.time = time;
		}
		public String getUnit() {
			return unit;
		}
		public void setUnit(String unit) {
			this.unit = unit;
		}
	}
}
