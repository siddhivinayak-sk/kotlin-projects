package com.sk.gateway.route;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouteConfig {
	@Bean
	public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
		return builder.routes()
				 .route(p -> p
				            .path("/get")
				            .filters(f -> f.addRequestHeader("Hello", "World"))
				            .uri("http://httpbin.org"))
				.build();
	}

}
