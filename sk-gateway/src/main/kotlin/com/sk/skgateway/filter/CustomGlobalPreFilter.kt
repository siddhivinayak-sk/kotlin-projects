package com.sk.skgateway.filter

import org.springframework.cloud.gateway.filter.GatewayFilterChain
import org.springframework.cloud.gateway.filter.GlobalFilter
import org.springframework.cloud.gateway.route.Route
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils
import org.springframework.core.Ordered
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

//@Component
class CustomGlobalPreFilter: GlobalFilter, Ordered {
    override fun filter(exchange: ServerWebExchange, chain: GatewayFilterChain): Mono<Void> {
        return Mono.defer {
            with(exchange.request) {
                val request: ServerHttpRequest = this.mutate().headers {
                    it.set("MyCustomHeader1", "MyCustomHeader1.Value")
                    it.set("MyCustomHeader2", "MyCustomHeader2.Value")
                    it.set("MyCustomHeader3", "MyCustomHeader3.Value")
                }.build()
                println("CustomGlobalPreFilter0: RouteId: ${(exchange.attributes[ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR] as Route).id}  RequestId: ${exchange.request.id}")
                println("CustomGlobalPreFilter1: ${exchange.attributes["Line21"]}")
                if(null == exchange.attributes["Line21"]) {
                    exchange.attributes["Line21"] = "LineValue-${Math.random()}"
                }
                chain.filter(exchange.mutate().request(this).build())
            }
        }
    }

    override fun getOrder(): Int {
        return -1
    }
}