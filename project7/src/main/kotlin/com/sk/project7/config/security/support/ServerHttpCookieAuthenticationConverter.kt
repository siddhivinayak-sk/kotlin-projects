package com.sk.project7.config.security.support

import org.springframework.security.core.Authentication
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import java.util.function.Function
import java.util.function.Predicate

class ServerHttpCookieAuthenticationConverter(private val jwtVerifyHandler: JwtVerifyHandler): Function<ServerWebExchange, Mono<Authentication>> {

    companion object {
        private const val BEARER = "Bearer "
        private val matchBearerLength = Predicate { authValue: String -> authValue.length > BEARER.length }
        private val isolateBearerValue =
                Function { authValue: String ->
                    Mono.justOrEmpty(
                            authValue.substring(BEARER.length)
                    )
                }
        fun extract(serverWebExchange: ServerWebExchange): Mono<String?> {
            val cookieSes = serverWebExchange.request
                    .cookies
                    .getFirst("X-Session-Id")
            return if (cookieSes != null) Mono.justOrEmpty(cookieSes.value) else Mono.empty()
        }
    }


    override fun apply(serverWebExchange: ServerWebExchange): Mono<Authentication> {
        return Mono.justOrEmpty(serverWebExchange)
                .flatMap(ServerHttpCookieAuthenticationConverter::extract)
                .flatMap {jwtVerifyHandler.check(it ?: "")}
                .flatMap(CurrentUserAuthenticationBearer::create)
    }
}