package com.sk.ktl.backpressure

import reactor.core.publisher.ConnectableFlux
import reactor.core.publisher.Flux

fun main(args: Array<String>) {
    val publish: ConnectableFlux<String> = Flux.create<String> {
        while(true) {
            it.next("${System.currentTimeMillis()}")
        }
    }
    .doOnNext {
        println("=> $it")
    }
    .publish()
    publish.connect()

}