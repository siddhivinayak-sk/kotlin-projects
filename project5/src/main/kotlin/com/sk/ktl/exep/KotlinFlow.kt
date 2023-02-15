package com.sk.ktl.exep

import reactor.core.publisher.Mono

fun main(args: Array<String>) {
    test()
}


fun returnMono(empty: Boolean): Mono<String> {
    return if(!empty) Mono.just("test") else Mono.empty()
}

fun test() {
    returnMono(false)
            .map {
                println("P1: $it")
                it
            }
            .map {
                val x = 5 / 0
                println("P2: $it")
                it
            }
            
//            .switchIfEmpty(
//                    Mono.error(NoSuchFieldError())
//            )
            .doOnError {
                println("P3: $it")
            }
            .map {
                println("P4: $it")
            }
            .subscribe()
}