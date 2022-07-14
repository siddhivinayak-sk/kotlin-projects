package com.sk.project7.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.web.reactive.function.server.RouterFunctions

@Configuration
class WebConfiguration {

    @Bean
    fun resRouter() = RouterFunctions.resources("/static/**", ClassPathResource("static/"))

}