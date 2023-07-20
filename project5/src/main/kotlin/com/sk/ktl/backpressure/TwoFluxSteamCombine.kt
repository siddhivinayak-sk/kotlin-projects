package com.sk.ktl.backpressure

import reactor.core.publisher.Flux

fun main(args: Array<String>) {
    Flux.just(1, 2, 3, 4, 5)
            .log()
            .zipWith(Flux.range(1, 1000))
            .map { "${it.t1} and ${it.t2}" }
            .doOnNext { println("$it") }
            .subscribe()
}