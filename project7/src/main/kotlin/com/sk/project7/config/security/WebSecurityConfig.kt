package com.sk.project7.config.security

import com.sk.project7.config.security.support.JwtVerifyHandler
import com.sk.project7.config.security.support.ServerHttpBearerAuthenticationConverter
import com.sk.project7.config.security.support.ServerHttpCookieAuthenticationConverter
import com.sk.project7.service.CustomUserDetailsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.SecurityWebFiltersOrder
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.authentication.AuthenticationWebFilter
import org.springframework.security.web.server.authorization.AuthorizationWebFilter
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
class WebSecurityConfig {

    @Value("\${jwt.secret}")
    private val jwtSecret: String = ""

    @Autowired
    lateinit var userDetailsService: CustomUserDetailsService

    @Bean
    fun securityWebFilterChain(
            http: ServerHttpSecurity,
            authManager: AuthenticationManager
    ): SecurityWebFilterChain? {
        return http.authorizeExchange()
                .pathMatchers(HttpMethod.OPTIONS).permitAll()
                .pathMatchers("/authenticate/**", "/actuator/**").permitAll()
                .pathMatchers("/event-emitter").hasRole("admin_role")
                .anyExchange().authenticated()
                .and()
                .formLogin().disable()
                .csrf().disable()
                .httpBasic().disable()
                .exceptionHandling()
                .authenticationEntryPoint { exchange, ex ->
                    Mono.fromRunnable {
                        val buffer = exchange.response.bufferFactory().wrap(ex.message!!.toByteArray())
                        exchange.response.writeWith(Flux.just(buffer))
                        exchange.response.statusCode = HttpStatus.UNAUTHORIZED
                    }
                }
                .accessDeniedHandler { exchange, ex ->
                    Mono.fromRunnable {
                        val buffer = exchange.response.bufferFactory().wrap(ex.message!!.toByteArray())
                        exchange.response.writeWith(Flux.just(buffer))
                        exchange.response.statusCode = HttpStatus.FORBIDDEN
                    }
                }
                .and()
                .addFilterAt(bearerAuthenticationFilter(authManager), SecurityWebFiltersOrder.AUTHENTICATION)
                .addFilterAt(cookieAuthenticationFilter(authManager), SecurityWebFiltersOrder.AUTHENTICATION)
                .build()
    }

    fun bearerAuthenticationFilter(authManager: AuthenticationManager?): AuthenticationWebFilter? {
        val bearerAuthenticationFilter = AuthenticationWebFilter(authManager)
        bearerAuthenticationFilter.setAuthenticationConverter(ServerHttpBearerAuthenticationConverter(JwtVerifyHandler(jwtSecret)))
        bearerAuthenticationFilter.setRequiresAuthenticationMatcher(ServerWebExchangeMatchers.pathMatchers("/**"))
        return bearerAuthenticationFilter
    }

    fun cookieAuthenticationFilter(authManager: AuthenticationManager?): AuthenticationWebFilter? {
        val cookieAuthenticationFilter = AuthenticationWebFilter(authManager)
        cookieAuthenticationFilter.setAuthenticationConverter(ServerHttpCookieAuthenticationConverter(JwtVerifyHandler(jwtSecret)))
        cookieAuthenticationFilter.setRequiresAuthenticationMatcher(ServerWebExchangeMatchers.pathMatchers("/**"))
        return cookieAuthenticationFilter
    }
}