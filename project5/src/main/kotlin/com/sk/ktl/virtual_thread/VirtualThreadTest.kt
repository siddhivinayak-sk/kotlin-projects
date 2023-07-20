package com.sk.ktl.virtual_thread

import com.sk.ktl.basic.CommonConstants.printString
import reactor.core.publisher.Mono

fun main(args: Array<String>) {

    create("test")
            .flatMap { it.transform0() } //exception at outer pipe runs only outer doOnError
            .flatMap { it.transform1() }
            .flatMap { out ->
                        out.transform2()
                                //.flatMap { it.transform0() } //exception at inner pipe runs both doOnError
                        .flatMap { it.transform3() }

                        .doOnSubscribe {
                            println("inner doOnSubscribe")
                        }
                        .doOnError {
                            println("inner doOnError")
                        }
            }
            .doOnSubscribe {
                println("doOnSubscribe")
            }
            .doOnError {
                println("doOnError")
            }
            .subscribe()


}


fun create(data: String) = Mono.just(data)

fun String.transform0() = Mono.just(this.substring(100) )

fun String.transform1() = Mono.just("$this transform1")

fun String.transform2() = Mono.just("$this transform2")

fun String.transform3() = Mono.just(this).map { printString("$this transform3") }

