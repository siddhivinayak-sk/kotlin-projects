package com.sk.ktl

import org.junit.jupiter.api.Test
import org.reactivestreams.Subscription
import reactor.core.publisher.BaseSubscriber
import reactor.core.publisher.Flux
import reactor.test.StepVerifier

class BackpressureTest {

    @Test
    fun whenRequestingChunks10_thenMessagesAreReceived() {
        val request: Flux<Int> = Flux.range(1, 50)
            .doOnSubscribe { println("===========DoOnSubscribe: $it==========") }
            .doOnRequest { println("------------DoOnRequest------------") }
            .doOnCancel { println(">>>>>>>>>>>DoOnCancel<<<<<<<<<<<<") }
            .doOnComplete { println("<<<<<<<<<<DOOnComplete<<<<<<<<<<<") }
            .doOnEach { println("///////////DoOnEach///////////") }
            .doOnError { println("@@@@@@@@@@@@DoOnError@@@@@@@@@@@@@") }
            .doOnTerminate { println("%%%%%%%%%%DoOnTerminate%%%%%%%%%%%") }
            .doOnNext { println("DoOnNext-1: $it") }
            .map { it * 2 }
            .doOnNext { println("DoOnNext-2: $it") }
            .map { it / 2 }
            .doOnNext { println("DoOnNext-3: $it") }

        request.subscribe(
            { value -> println("Subscribe: $value")} ,
            {th -> th.printStackTrace()},
            {println("All 50 items processed successfully")},
            {subs ->
                for(i in 1..5) {
                    println("Requesting the next 10 elements")
                    subs.request(10)
                }
            }
        )

        StepVerifier
            .create(request)
            .expectSubscription()
            .thenRequest(10)
            .expectNext(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
            .thenRequest(10)
            .expectNext(11, 12, 13, 14, 15, 16, 17, 18, 19, 20)
            .thenRequest(10)
            .expectNext(21, 22, 23, 24, 25, 26, 27, 28, 29, 30)
            .thenRequest(10)
            .expectNext(31, 32, 33, 34, 35, 36, 37, 38, 39, 40)
            .thenRequest(10)
            .expectNext(41, 42, 43, 44, 45, 46, 47, 48, 49, 50)
            .verifyComplete()
    }

    @Test
    fun whenLimitRateSet_thenSplitIntoChunks() {
        val limit: Flux<Int> = Flux.range(1, 25)
            .doOnSubscribe { println("===========DoOnSubscribe: $it==========") }
            .doOnRequest { println("------------DoOnRequest------------") }
            .doOnCancel { println(">>>>>>>>>>>DoOnCancel<<<<<<<<<<<<") }
            .doOnComplete { println("<<<<<<<<<<DOOnComplete<<<<<<<<<<<") }
            .doOnEach { println("///////////DoOnEach///////////") }
            .doOnError { println("@@@@@@@@@@@@DoOnError@@@@@@@@@@@@@") }
            .doOnTerminate { println("%%%%%%%%%%DoOnTerminate%%%%%%%%%%%") }
            .doOnNext { println("DoOnNext-1: $it") }
            .map { it * 2 }
            .doOnNext { println("DoOnNext-2: $it") }
            .map { it / 2 }
            .doOnNext { println("DoOnNext-3: $it") }

        limit.limitRate(10)

        limit.subscribe (
            {value -> println("Subscribe: $value")},
            {th -> th.printStackTrace()},
            {println("Finished")},
            {subs -> subs.request(15)}
        )

        StepVerifier
            .create(limit)
            .expectSubscription()
            .thenRequest(15)
            .expectNext(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
            .expectNext(11, 12, 13, 14, 15)
            .thenRequest(10)
            .expectNext(16, 17, 18, 19, 20, 21, 22, 23, 24, 25)
            .verifyComplete()
    }

    @Test
    fun whenCancel_thenSubscriptionFinished() {
        val cancel: Flux<Int> = Flux.range(1, 10)
            .log()
            .doOnSubscribe { println("===========DoOnSubscribe: $it==========") }
            .doOnRequest { println("------------DoOnRequest------------") }
            .doOnCancel { println(">>>>>>>>>>>DoOnCancel<<<<<<<<<<<<") }
            .doOnComplete { println("<<<<<<<<<<DOOnComplete<<<<<<<<<<<") }
            .doOnEach { println("///////////DoOnEach///////////") }
            .doOnError { println("@@@@@@@@@@@@DoOnError@@@@@@@@@@@@@") }
            .doOnTerminate { println("%%%%%%%%%%DoOnTerminate%%%%%%%%%%%") }
            .doOnNext { println("DoOnNext-1: $it") }
            .map { it * 2 }
            .doOnNext { println("DoOnNext-2: $it") }
            .map { it / 2 }
            .doOnNext { println("DoOnNext-3: $it") }

        cancel.subscribe(
            object: BaseSubscriber<Int>() {
                override fun hookOnNext(value: Int) {
                    request(3)
                    println("VVV: $value")
                    cancel()
                }
            }
        )

        StepVerifier
            .create(cancel)
            .expectSubscription()
            .thenCancel()
            .verify()
    }


}