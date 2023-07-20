package com.sk.ktl.backpressure

import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription
import reactor.core.publisher.Flux

fun main(args: Array<String>) {
    Flux.range(1, 10)
            .log()
            .subscribe(object: Subscriber<Int> {
                var counter: Int = 0
                lateinit var subscriber: Subscription
                override fun onSubscribe(s: Subscription) {
                    subscriber = s
                    subscriber.request(2)
                }

                override fun onError(t: Throwable?) {
                    println("OnError called")
                }

                override fun onComplete() {
                    println("OnCompleted called")
                }

                override fun onNext(t: Int?) {
                    counter++
                    if(counter % 2 == 0) {
                        subscriber.request(2)
                    }
                }
            })
}