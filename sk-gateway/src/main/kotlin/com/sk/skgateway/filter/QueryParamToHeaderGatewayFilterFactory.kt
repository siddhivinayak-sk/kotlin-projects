package com.sk.skgateway.filter

import org.springframework.cloud.gateway.filter.GatewayFilter
import org.springframework.cloud.gateway.filter.GatewayFilterChain
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
class QueryParamToHeaderGatewayFilterFactory: AbstractGatewayFilterFactory<QueryParamToHeaderGatewayFilterFactory.QueryParamToHeaderFilterProperties>(QueryParamToHeaderFilterProperties::class.java) {

    override fun apply(config: QueryParamToHeaderFilterProperties?): GatewayFilter {
        return OrderedGatewayFilter( { exchange: ServerWebExchange, chain: GatewayFilterChain ->
                                         Mono.just(null != exchange.request.queryParams[config!!.paramName])
                                                 .filter { it }
                                                 .flatMap { chain.filter(exchange.mutate().request(exchange.request.mutate().headers { headers ->
                                                     headers[config.paramName] = exchange.request.queryParams[config.paramName]
                                                 }.build()).build()) }
                                                 .switchIfEmpty(chain.filter(exchange))
                                     }, -1)
    }

    override fun shortcutFieldOrder() = QueryParamToHeaderFilterProperties.configFieldsOrder

    class QueryParamToHeaderFilterProperties {
        lateinit var paramName: String

        companion object {
            val configFieldsOrder = listOf("paramName")
        }
    }
}