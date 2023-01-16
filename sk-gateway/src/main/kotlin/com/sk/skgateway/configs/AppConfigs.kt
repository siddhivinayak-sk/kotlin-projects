package com.sk.skgateway.configs

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import reactor.core.publisher.Mono

@Configuration
class AppConfigs {

//    @Bean
//    fun iPKeyResolver(): IPKeyResolver = IPKeyResolver()

    @Bean
    @Primary
    fun keyResolver1(): KeyResolver? {
        println("KeyResolver 1: ")
        return KeyResolver { exchange ->
            println("KeyResolver 1: ")
            Mono.just("1")
        }
    }

    @Bean
    fun keyResolver2(): KeyResolver? {
        return KeyResolver { exchange ->
            println("KeyResolver 2: ")
            Mono.just("2")
        }
    }

    @Bean
    fun keyResolver3(): KeyResolver? {
        return KeyResolver { exchange ->
            println("KeyResolver 3: ")
            Mono.just("3")
        }
    }
}