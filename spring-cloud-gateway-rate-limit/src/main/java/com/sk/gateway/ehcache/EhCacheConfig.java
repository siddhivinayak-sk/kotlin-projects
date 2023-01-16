package com.sk.gateway.ehcache;

import static com.sk.gateway.filter.FilterConstants.BUCKET4J_EHCACHE_ENABLED;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
@EnableCaching
@ConditionalOnProperty(BUCKET4J_EHCACHE_ENABLED)
public class EhCacheConfig {
    @Bean
    public EhCacheCacheManager ehCacheCacheManager() {
    	EhCacheManagerFactoryBean ecmfb = new EhCacheManagerFactoryBean();
	    ecmfb.setConfigLocation(new ClassPathResource("ehcache.xml"));
	    ecmfb.afterPropertiesSet();
        EhCacheCacheManager eccm = new EhCacheCacheManager();
        eccm.setCacheManager(ecmfb.getObject());
        eccm.afterPropertiesSet();
        return eccm;
    }	

}
