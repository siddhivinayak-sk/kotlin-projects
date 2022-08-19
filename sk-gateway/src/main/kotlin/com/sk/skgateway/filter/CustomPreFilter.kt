package com.sk.skgateway.filter

import org.springframework.cloud.gateway.filter.GatewayFilter
import org.springframework.cloud.gateway.filter.GatewayFilterChain
import org.springframework.core.Ordered
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

class CustomPreFilter: GatewayFilter, Ordered {
    override fun filter(exchange: ServerWebExchange?, chain: GatewayFilterChain?): Mono<Void> {
        return Mono.defer {
            chain?.filter(exchange)
//            with(exchange) {
//                chain?.filter(exchange)
//            }
        }
    }

    override fun getOrder(): Int {
        return 3
    }
}