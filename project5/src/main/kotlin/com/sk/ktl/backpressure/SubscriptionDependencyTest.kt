package com.sk.ktl.backpressure

import java.security.MessageDigest
import reactor.core.publisher.Mono

fun main(args: Array<String>) {

    Mono.just("\npipe1")
        .map { pipe2(); it }
        .doOnNext { print("success L1") }
        .subscribe(System.out::println)
    Mono.just("\npipe3").subscribe(System.out::println)
}

fun pipe2() {
    Mono.just("\npipe2")
        .map {
            var x = 0
            for (i in 1..100000) {
                x = i + i
                sha256("mytext$i")
            }
            println(x)
            it
        }
        .doOnSuccess { println("Success L2") }
        .subscribe(System.out::println)
}


fun sha256(input: String): String {
    val bytes = MessageDigest.getInstance("SHA-256").digest(input.toByteArray())
    return bytes.joinToString("") { "%02x".format(it) }
}