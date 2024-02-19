package com.sk.skgateway.filter

import org.slf4j.LoggerFactory
import org.springframework.cloud.gateway.filter.GatewayFilter
import org.springframework.cloud.gateway.filter.GatewayFilterChain
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import java.io.File

@Component
class RequestLogGatewayFilterFactory: AbstractGatewayFilterFactory<RequestLogGatewayFilterFactory.RequestLogGatewayFilterProperties>(RequestLogGatewayFilterProperties::class.java) {

    private val logger = LoggerFactory.getLogger(RequestLogGatewayFilterFactory::class.java)
    private val equal = "="
    private val lineSeparator = "\n\r"

    override fun apply(config: RequestLogGatewayFilterProperties?): GatewayFilter {
        return OrderedGatewayFilter( { exchange: ServerWebExchange, chain: GatewayFilterChain ->
            val log = StringBuilder()
            log.append("===Request started: ${exchange.request.id} ${exchange.request.method} ${exchange.request.uri}===$lineSeparator")
            log.append("Query Params:$lineSeparator")
            exchange.request.queryParams.entries.stream().forEach {
                log.append(it.key)
                log.append(equal)
                log.append(it.value.joinToString())
            }
             log.append("Headers:$lineSeparator")
             exchange.request.headers.entries.stream().forEach {
                 log.append(it.key)
                 log.append(equal)
                 log.append(it.value.joinToString())
             }
             log.append("$lineSeparator===Request ended===")
            logger.info(log.toString())
            chain.filter(exchange)
        }, config?.order ?: 0)
    }

    override fun shortcutFieldOrder() = RequestLogGatewayFilterProperties.configFieldsOrder

    class RequestLogGatewayFilterProperties {
        var order: Int = 0

        companion object {
            val configFieldsOrder = listOf("order")
        }
    }
}