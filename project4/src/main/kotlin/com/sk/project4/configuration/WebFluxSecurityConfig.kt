package com.sk.project4.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.server.SecurityWebFilterChain

@Configuration
@EnableWebFluxSecurity
class WebFluxSecurityConfig {

    @Bean
    fun userDetailsService(): MapReactiveUserDetailsService {
        val user: UserDetails = User
                .withUsername("user")
            .password(passwordEncoder().encode("userpwd"))
            .roles("USER")
            .build()

        val admin: UserDetails = User
                .withUsername("admin")
            .password(passwordEncoder().encode("adminpwd"))
            .roles("ADMIN")
            .build()
        return MapReactiveUserDetailsService(user, admin)
    }

    @Bean
    fun securityWebFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
        return http
            .authorizeExchange()
            .pathMatchers("/v3/api-docs/**").permitAll()
            .pathMatchers("/person/**")
            .hasAuthority("ROLE_ADMIN")
            .anyExchange()
            .authenticated()
            .and().httpBasic()
            .and().csrf().disable()
            .build();
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder();
    }

}