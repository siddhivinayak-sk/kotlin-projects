package com.sk.ktl.backpressure

import reactor.core.publisher.Flux
import reactor.core.scheduler.Schedulers

fun main(args: Array<String>) {
    Flux.just(1, 2, 3, 4, 5)
            .log()
            .map { it * 2 }
            .subscribeOn(Schedulers.parallel())
            .subscribe()
}