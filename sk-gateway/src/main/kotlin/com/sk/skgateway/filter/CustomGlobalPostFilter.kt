package com.sk.skgateway.filter

import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory
import org.springframework.cloud.gateway.filter.GatewayFilterChain
import org.springframework.cloud.gateway.filter.GlobalFilter
import org.springframework.cloud.gateway.route.Route
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils
import org.springframework.core.Ordered
import org.springframework.http.HttpStatus
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
class CustomGlobalPostFilter: GlobalFilter, Ordered {
    override fun filter(exchange: ServerWebExchange, chain: GatewayFilterChain?): Mono<Void>? {
        return chain?.filter(exchange)
            ?.then(Mono.just(exchange))
            ?.map {
                it!!.process()
            }
            ?.map {
                println("CustomGlobalPostFilter0: RouteId: ${(exchange.attributes[ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR] as Route).id}  RequestId: ${exchange.request.id}")
                println("CustomGlobalPostFilter1: ${it.request.headers["MyCustomHeader1"]}")
                println("CustomGlobalPostFilter2: ${exchange.attributes["Line21"]}")
            }
            ?.then()
    }

    override fun getOrder(): Int {
        return 0
    }

    private fun ServerWebExchange.process(): ServerWebExchange {
        val response: ServerHttpResponse = this.response
        try {
            response?.headers?.add("MyCustomHeaderY", "MyCustomHeaderYValue")
        }
        catch(e: java.lang.UnsupportedOperationException) {
            println("modification in response not supported: $response")
        }
        return mutate().response(response).build()
    }
}