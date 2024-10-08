package com.sk.ktl.reactive

import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.Optional

fun main(args: Array<String>) {
//    Mono.just("String data")
//            .map {
//                println("$it")
//                "$it is example"
//            }
//            .subscribe {
//                println(it)
//            }
//    Flux.range(1, 10)
//            //.filter{it > 10}
//            .map { it * 2}
//            //.count()
//            .doOnNext {
//                println("$it")
//            }
//            .doOnSubscribe {
//                println("doOnSubscribe $it")
//            }
//            .doOnComplete {
//                println("doOnComplete ")
//            }
//            .doOnError {
//                println("doOnError $it")
//            }
//            .doOnCancel {
//                println("doOnCancel ")
//            }
//            .doOnEach {
//                println("doOnEach $it")
//            }
//            .subscribe()
//    Mono.just("String")
//            .filter { it.length > 100 }
//            .switchIfEmpty(Mono.error(IllegalStateException("No string found")))
//            .subscribe()
//    Flux.range(1, 100)
//            .take(3)
//            .subscribe { println("$it")

    Mono.zip(
            Mono.just("1"),
            Mono.just("1").flatMap { mcalc() },
    )
            .doOnSuccess { println(it) }
            .doOnError { print("error") }
            .subscribe()


}

private fun calc(): String? {
    return null
}

private fun mcalc(): Mono<String?> {
    return Mono.empty()
}