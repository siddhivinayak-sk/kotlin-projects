package com.sk.gateway.jedis;

import static com.sk.gateway.filter.FilterConstants.JEDIS_CUSTOM_ENABLED;

import java.util.List;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;

@Configuration
@ConditionalOnProperty(JEDIS_CUSTOM_ENABLED)
public class CustomJedisConfig {

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
	public DefaultRedisScript<String> defaultRedisScript() {
		DefaultRedisScript<String> script =  new DefaultRedisScript<String>();
		script.setLocation(new ClassPathResource("rate_limit.lua"));
		script.afterPropertiesSet();
		return script;
	}
	
}
