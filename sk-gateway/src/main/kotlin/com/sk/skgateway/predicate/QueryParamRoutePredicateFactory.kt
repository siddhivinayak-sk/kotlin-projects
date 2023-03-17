package com.sk.skgateway.predicate

import org.slf4j.LoggerFactory
import org.springframework.cloud.gateway.handler.predicate.AbstractRoutePredicateFactory
import org.springframework.cloud.gateway.handler.predicate.GatewayPredicate
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils
import org.springframework.stereotype.Component
import org.springframework.validation.annotation.Validated
import org.springframework.web.server.ServerWebExchange
import java.util.Map
import java.util.function.Predicate
import javax.validation.constraints.NotEmpty

@Component
class QueryParamRoutePredicateFactory : AbstractRoutePredicateFactory<QueryParamRoutePredicateFactory.Config>(Config::class.java) {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun shortcutFieldOrder(): List<String> {
        return listOf(PARAM_KEY)
    }

    override fun apply(config: Config?): Predicate<ServerWebExchange> {
        return object : GatewayPredicate {
            override fun test(exchange: ServerWebExchange): Boolean {
                if (exchange.request.queryParams.containsKey(config!!.param)) {
                    val value = exchange.request.queryParams.getFirst(config.param!!)
                    logger.debug("Saving {}={}", config.param, value)
                    ServerWebExchangeUtils.putUriTemplateVariables(exchange, Map.of(config.param, value))
                    return true
                }
                logger.debug("Query parameter {} not found.", config.param)
                return false
            }

            override fun toString(): String {
                return String.format("Query: param=%s", config!!.param)
            }
        }
    }

    @Validated
    class Config {
        var param: @NotEmpty String = ""

        fun setParam(param: String): Config {
            this.param = param
            return this
        }
    }

    companion object {
        const val PARAM_KEY = "param"
    }
}