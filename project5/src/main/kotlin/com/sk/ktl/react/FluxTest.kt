package com.sk.ktl.react

import org.reactivestreams.Subscription
import reactor.core.publisher.Flux
import java.time.Duration

fun main(args: Array<String>) {
    intervalTest()
}






fun intervalTest() {
    Thread {
        Flux.interval(Duration.ofMillis(1000L))
            .doOnNext { println(it) }
            .map { "Number-$it" }
            .doOnNext { println(it) }
            .subscribe()
    }.start()
    Thread.sleep(50000)
}

