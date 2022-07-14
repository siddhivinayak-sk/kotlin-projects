package com.sk.project7

import com.sk.project7.config.ReactiveWebSocketConfiguration
import com.sk.project7.config.WebConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Import
import org.springframework.web.reactive.config.EnableWebFlux

@SpringBootApplication
@EnableWebFlux
class Project7Application

fun main(args: Array<String>) {
	runApplication<Project7Application>(*args)
}
