package com.sk.skgateway

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["com.sk.skgateway"])
class SkGatewayApplication

fun main(args: Array<String>) {
	runApplication<SkGatewayApplication>(*args)
}
