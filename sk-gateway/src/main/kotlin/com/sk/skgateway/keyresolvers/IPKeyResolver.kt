package com.sk.skgateway.keyresolvers

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

class IPKeyResolver: KeyResolver {
    override fun resolve(exchange: ServerWebExchange?): Mono<String>? {
        println("IPKeyResolver Request: ${exchange!!.request.remoteAddress?.hostName}")
        return exchange!!.request.remoteAddress?.hostName.toMono()
    }
}