package com.sk.ktl.exep

import reactor.core.publisher.Mono

fun main(args: Array<String>) {
    process()
            //.onErrorResume { Mono.just("onErrorResume") }
            .doOnError { println("doOnError") }
            .doOnSubscribe { println("doOnSubscribe") }
            .doOnSuccess { println("doOnSuccess") }
            .doOnNext { println("doOnNext") }
            .subscribe { println(it) }
}



fun datasource(): Mono<String> {
    val ex = Exception("datasource exception")
    return Mono.just("data")
            .map { if(it.equals("data1")) throw ex else it }
            .flatMap { if(it.equals("data2")) Mono.error(ex) else Mono.just(it) }
}

fun process(): Mono<String> {
    return datasource()
            .onErrorResume {
                Mono.error { Exception("my ex: " + it.message) }
            }
}