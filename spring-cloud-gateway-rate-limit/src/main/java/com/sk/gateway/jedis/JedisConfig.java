package com.sk.gateway.jedis;

import static com.sk.gateway.filter.FilterConstants.BUCKET4J_JEDIS_ENABLED;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
@ConditionalOnProperty(BUCKET4J_JEDIS_ENABLED)
public class JedisConfig {

	@Bean
	LettuceConnectionFactory lettuceConnectionFactory() {
		LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory();
		lettuceConnectionFactory.setHostName("localhost");
		lettuceConnectionFactory.setPort(6379);
		return lettuceConnectionFactory;
	}
	
	@Bean
	public ReactiveStringRedisTemplate reactiveStringRedisTemplate() {
		ReactiveStringRedisTemplate template = new ReactiveStringRedisTemplate(lettuceConnectionFactory());
		return template;
	}	
	
	@Bean
	JedisConnectionFactory jedisConnectionFactory() {
		JedisConnectionFactory jedisConFactory
	      = new JedisConnectionFactory();
	    jedisConFactory.setHostName("localhost");
	    jedisConFactory.setPort(6379);
	    return jedisConFactory;
	}

	@Bean
	public RedisTemplate redisTemplate() {
	    RedisTemplate template = new RedisTemplate();
	    template.setConnectionFactory(jedisConnectionFactory());
	    return template;
	}	

	
	
}
