package com.sk.skgateway.filter

import org.springframework.cloud.gateway.filter.GatewayFilter
import org.springframework.cloud.gateway.filter.GatewayFilterChain
import org.springframework.core.Ordered
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

class CustomPostFilter: GatewayFilter, Ordered {
    override fun filter(exchange: ServerWebExchange?, chain: GatewayFilterChain?): Mono<Void> {
        TODO("Not yet implemented")
    }

    override fun getOrder(): Int {
        return 2
    }
}