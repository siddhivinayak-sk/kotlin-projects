package com.sk.project4.configuration

import com.sk.project4.handler.PersonHandler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.config.EnableWebFlux
import org.springframework.web.reactive.config.WebFluxConfigurer
import org.springframework.web.reactive.function.server.RequestPredicates
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.reactive.function.server.ServerResponse

@Configuration
@EnableWebFlux
class WebFluxConfig: WebFluxConfigurer {


    lateinit var handler: PersonHandler

    @Autowired
    fun setPersonHandler(personHandler: PersonHandler) {
        this.handler = personHandler
    }


    @Bean
    fun routerFunctionA(): RouterFunction<ServerResponse> {
        return RouterFunctions.route()
            .path("/person") { builder ->
                builder
                    .GET("/{id}", RequestPredicates.accept(MediaType.APPLICATION_JSON), handler::getPerson)
                    .POST(RequestPredicates.accept(MediaType.APPLICATION_JSON), handler::createPerson)
                    .PUT("/{id}", RequestPredicates.accept(MediaType.APPLICATION_JSON), handler::updatePerson)
                    .DELETE("/{id}", RequestPredicates.accept(MediaType.APPLICATION_JSON), handler::deletePerson)

            }.build()
    }

}