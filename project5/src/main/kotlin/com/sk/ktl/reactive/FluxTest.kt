package com.sk.ktl.reactive

import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

fun main(args: Array<String>) {
    testFluxStaticMethods()
}

fun testFluxStaticMethods() {
    val flux1 = Flux.combineLatest({ inArr -> inArr.joinToString { "-" } }, 2, Mono.just("1"), Mono.just("2"), Mono.just("3"), Mono.just("4"))
    flux1.subscribe(::println, ::println, ::println)
}