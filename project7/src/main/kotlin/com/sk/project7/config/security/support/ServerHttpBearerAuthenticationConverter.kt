package com.sk.project7.config.security.support

import org.springframework.http.HttpHeaders
import org.springframework.security.core.Authentication
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import java.util.function.Function
import java.util.function.Predicate

class ServerHttpBearerAuthenticationConverter(private val jwtVerifyHandler: JwtVerifyHandler): Function<ServerWebExchange, Mono<Authentication>> {

    companion object {
        const val BEARER = "Bearer "
        var matchBearerLength = Predicate { authValue: String -> authValue.length > BEARER.length }
        var isolateBearerValue = Function { authValue: String ->
                    Mono.justOrEmpty(
                            authValue.substring(BEARER.length)
                    )
                }

        fun extract(serverWebExchange: ServerWebExchange): Mono<String> {
            return Mono.justOrEmpty(serverWebExchange.request
                                            .headers
                                            .getFirst(HttpHeaders.AUTHORIZATION))
        }
    }

    override fun apply(serverWebExchange: ServerWebExchange): Mono<Authentication> {
        return Mono.justOrEmpty(serverWebExchange)
                .flatMap(ServerHttpBearerAuthenticationConverter::extract)
                .filter(matchBearerLength)
                .flatMap(isolateBearerValue)
                .flatMap {
                    jwtVerifyHandler.check(it)
                }
                .flatMap(CurrentUserAuthenticationBearer::create)
    }
}