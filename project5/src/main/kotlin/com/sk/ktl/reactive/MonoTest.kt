package com.sk.ktl.reactive

import io.micrometer.context.ContextRegistry
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription
import reactor.core.CoreSubscriber
import reactor.core.Scannable
import reactor.core.observability.SignalListener
import reactor.core.observability.SignalListenerFactory
import reactor.core.publisher.Flux
import reactor.core.publisher.Hooks
import reactor.core.publisher.Mono
import reactor.core.publisher.SignalType
import reactor.core.scheduler.Schedulers
import reactor.util.context.Context
import reactor.util.retry.Retry
import java.time.Duration
import java.util.Arrays
import java.util.concurrent.CompletableFuture
import java.util.function.Supplier
import kotlin.jvm.optionals.getOrNull

fun main(args: Array<String>) {
    Hooks.enableAutomaticContextPropagation()
    Hooks.enableContextLossTracking()
    Hooks.onOperatorDebug()

    val CORRELATION_ID = ThreadLocal<String>()

    ContextRegistry.getInstance().registerThreadLocalAccessor(
        "CORRELATION_ID",
        CORRELATION_ID::get,
        CORRELATION_ID::set,
        CORRELATION_ID::remove
    )

    //useOfAnd()
    //useOfAs()
    //useOfBlock()
    //useOfCache()
    //useOfCancelOn()
    //useOfCast()
    //useOfCast()
    //useOfCast()
    //useOfCheckpoint()
    //useOfConcateWith()
    //useOfContextCapture()
    //useOfContextWrite()
    //useOfHandle()
    //useOfHide()
    //useOfName()
    //useOfOfType()
    //useOfOnTerminateDetach()
    //useOfOr()
    //useOfPublish()
    //useOfPublishOnAndSubscribeOn()
    //useOfRepeat()
    //useOfRetry()
    //useOfShare()
    //useOfSingle()
    //useOfTag()
    //useOfTag()
    //useOfTake()
    //useOfTap()
    useOfTimed()
}


fun useOfAnd() {
    val source = Mono.just("str1")
        .doOnSuccess { println("Source mono is done") }
    Mono.just("Str2")
        .doOnSuccess { println("Another mono is done") }
        .and(source) //joins termination signal of both Mono
        .subscribe()
}

fun useOfAs() {
    Mono.just("str")
        .`as` { input -> input.map { "$it transformed" } } //as(...) transform the Mono<?> with passed transformer function
        .subscribe { println(it) }
}

fun useOfBlock() {
    val string = Mono.just("str")
        .block() //blocks indefinitely until next signal is received
    println(string)

    val string2 = Mono.just("str")
        .block(Duration.ofSeconds(100L)) //blocks till given duration or next signal is received
    println(string2)

    val optional3 = Mono.just("str")
        .blockOptional() //blocks till indefinitely until next signal is received
    println(optional3.getOrNull())

    val optional4 = Mono.just("str")
        .blockOptional(Duration.ofSeconds(100L)) //blocks to given duration or next signal is received
    println(optional4.getOrNull())
}

fun useOfCache() {
    val mono = Mono.fromCallable {
        println("creating mono")
        5
    }.map {
        println("mapping")
        it * 2
    }
    val cached1 =  mono.cache()

    println("Cached 1: ${cached1.block()}")
    println("Cached 2: ${cached1.block()}")
    println("Cached 3: ${cached1.block()}")

    println("No Cache 1: ${mono.block()}")
    println("No Cache 2: ${mono.block()}")
    println("No Cache 3: ${mono.block()}")

    val cached2 = mono.cache(Duration.ofSeconds(1L))
    val cached3 = mono.cache(Duration.ofSeconds(1L), Schedulers.boundedElastic())
    val cached4 = mono.cache(
        { Duration.ofSeconds(1L) }, //TTL for source
        { it.printStackTrace(); Duration.ofSeconds(1L) }, //TTL for source if error
        { Duration.ofSeconds(1L) } //TTL for source if empty
    )
    val cached5 = mono.cache(
        { Duration.ofSeconds(1L) }, //TTL for source
        { it.printStackTrace(); Duration.ofSeconds(1L) }, //TTL for source if error
        { Duration.ofSeconds(1L) } //TTL for source if empty
        , Schedulers.boundedElastic()
    )
    val cached6 = mono.cacheInvalidateIf { it == 10 }
    val cahced7 = mono.cacheInvalidateWhen { Mono.empty<Void>() }
    val cahced8 = mono.cacheInvalidateWhen ({ Mono.empty<Void>() }, { println("chache invalidated: $it") })
}

fun useOfCancelOn() {
    val flux = Flux.interval(Duration.ofSeconds(1L))
        .cancelOn(Schedulers.boundedElastic()) //cancels the subscription on provided scheduler

    flux.subscribeOn(Schedulers.boundedElastic())
        .log()
        .take(2) //take 2 elements then cancel signal emitted
        .subscribe(
        { println("Element consumed: $it") },
        { println("Exception: ${it.printStackTrace()}") },
        { println("Subscription completed") }
    )

    Thread.sleep(5000L)
}

fun useOfCast() {
    val mono:Mono<*> = Mono.just(Object())
    val mono2 = mono.map { it as String } //cast the Mono<?> to given type
    val mono3 = mono.cast(String::class.java) //cast the Mono<?> to given type (throws ClassCastException if not possible)
    mono3.subscribe { println(it) }
}

fun useOfCheckpoint() {
    val mono = Mono.just("str").flatMap { Mono.error<IllegalStateException>(IllegalStateException()) }
    val mono2 = mono.checkpoint() //full assembly tracing if error at upstream
    mono2.subscribe { println(it) }

    val mono3 = mono.checkpoint("checkpoint description")
    mono3.subscribe { println(it) }

    val mono4 = mono.checkpoint("checkpoint description", true)
    mono4.subscribe { println(it) }
}

fun useOfConcateWith() {
    val mono1 = Mono.just("data1")
    val mono2 = Mono.just("data2")
    val flux = mono1.concatWith(mono2) //Concatenate two mono and return a flux
    flux.subscribe { println(it) }
}

private fun contextWrite(initialData: String, key: String, value: String) = Mono.just(initialData)
    .contextWrite { context ->
        Context.of(key, value)
    }
    .contextCapture()

private fun deferContextual(initialData: String) = Mono.deferContextual { Mono.just(it) }
    .map {
        it.forEach { t, u -> println("key: $t, value: $u") }
        it
    }.map { initialData }


fun useOfContextCapture() {
    contextWrite("data", "CORRELATION_ID", "CORRELATION_ID")
        .materialize()
        .doOnNext {
            println("ContextView: ${it.contextView}")
        }
        .dematerialize<String>()
        .subscribe()
}

fun useOfContextWrite() {
    Mono.just("Blank")
        .contextWrite { context ->
            val localContext = Context.of("key", "value")
            localContext.put("key", "value")
            localContext
        }
    .flatMap {
        deferContextual("").map { it }
    }
    .subscribe {
        println(it)
    }
}

fun useOfStaticMethod() {
    val mono1 = Mono.just("data1") //simple mono creation
    val mono2 = Mono.create<String> { //create mono with callback on sink
        it.onCancel { println("Cancelled") }
        it.onDispose { println("Disposed") }
        it.onRequest { println("Requested") }
        it.success("data2")
    }
    val mono3 = Mono.defer { mono1 } //create mono with another mono
    val mono4 = Mono.empty<String>() //create empty mono
    val mono5 = Mono.error<String>(IllegalStateException()) //create mono with error
    val mono6 = Mono.fromCallable { "data3" } //create mono from callable
    val mono7 = Mono.justOrEmpty<String>(null) //create mono from optional
    val mono8 = Mono.justOrEmpty<String>("data4") //create mono from optional
    val mono9 = Mono.deferContextual {
        println(it)
        mono1
    } //create mono with context view
    val mono10 = Mono.delay(Duration.ofSeconds(1L)) //create a Long type mono on delay of given duration
    val mono11 = Mono.delay(Duration.ofSeconds(1L), Schedulers.boundedElastic()) //create a Long type mono on delay of given duration on provided scheduler
    val mono12 = Mono.firstWithSignal(Arrays.asList(Mono.just("first"), Mono.just("Second"))) //create mono from first signal
    val mono13 = Mono.firstWithValue(Arrays.asList(Mono.just("first"), Mono.just("Second"))) //create mono from first value
    val mono14 = Mono.from<String> { "data" } // Create mono from supplier
    val mono15 = Mono.fromCompletionStage { CompletableFuture.supplyAsync { "data" } } //create mono from completion stage
    val mono16 = Mono.fromFuture(CompletableFuture.supplyAsync { "data" }) //create mono from future, if mono is cancelled then future is cancelled
    val mono17 = Mono.fromFuture(CompletableFuture.supplyAsync { "data" }, false) //create mono from future, if mono is cancelled then future is cancelled optoinally
    val mono18 = Mono.fromDirect<String> { mono1 } //create mono from publisher without cardinality check
    val mono19 = Mono.fromRunnable<String> {  } //create empty mono when runnable completes
    val mono20 = Mono.fromSupplier { "data" } //create mono from supplier
    val mono21 = Mono.ignoreElements(mono1) //create mono from ignoring elements from passed supplier but completes when source completes
    val mono22 = Mono.never<String>() //create mono that never emits signal, always in running state
    val mono23 = Mono.sequenceEqual(mono1, mono2) //create mono that emits true if both mono items are equal
    val mono24 = Mono.using({ "resource" }, { Mono.just("data") }, { println("dispose: $it") }) //create mono from resource supplier, data supplier and dispose callback
    val mono25 = Mono.usingWhen<String, String>({"resource"}, { Mono.just("resource closure") }, { Mono.just("resource Cleanup") }) //create mono from resource supplier for each individual subscriber
    val mono26 = Mono.`when`(mono1, mono2) //aggregate multiple mono into single mono, it will be completed when all publishers are completed
    val mono27 = Mono.whenDelayError(mono1, mono2) //aggregate multiple mono into single mono, it will be completed when all publishers are completed but error is delayed
    val mono28 = Mono.zip(mono1, mono2) //create mono of tuple from multiple mono
    val mono29 = Mono.zipDelayError(mono1, mono2) //create mono of tuple from multiple mono but error is delayed
}

fun useOfDelay() {
    val mono1 = Mono.delay(Duration.ofSeconds(1L))
    mono1.delayElement(Duration.ofSeconds(1L)) //delay the onNext(...) signal by given duration
    mono1.delaySubscription(Duration.ofSeconds(1L)) //delay the subscription by given duration
    mono1.delayUntil { Mono.just("") } //delay the subscription until the passed mono emits signal
}

fun useOfMaterializedAndDematerialized() {
    Mono.just("test")
        .materialize() //Transform incoming onNext, onError and onComplete signals into Signal instances, materializing these signals
        .doOnNext { it.contextView.forEach { t, u -> println("key: $t, value: $u") } }
        .dematerialize<String>() //Dematerialize the incoming Signal instances back to real signals
        .doOnNext { println(it) }
        .subscribe()
}

fun useOfDo() {
    Mono.just("data")
        .doOnNext {  }
        .doOnSubscribe {  }
        .doOnError {  }
        .doAfterTerminate {  }
        .doOnCancel {  }
        .doOnEach {  }
        .doOnRequest {  }
        .doOnSuccess {  }
        .doOnTerminate {  }
        .doFinally {  }
        .doFirst {  }
        .subscribe()
}

fun useOfElapsed() {
    Mono.just("data")
        .elapsed() //emit tuple of elapsed time and data
        .subscribe { println(it) }
}

fun useOfExpand() {
    Mono.just("data")
        .expand { Mono.just("data") } //Recursively expand elements into a graph and emit all the resulting element using a breadth-first traversal strategy.
        .subscribe { println(it) }

    Mono.just("data")
        .expandDeep { Mono.just("data") } //Recursively expand elements into a graph and emit all the resulting element using a depth-first traversal strategy.
        .subscribe { println(it) }
}

fun useOfFilter() {
    Mono.just("data")
        .filter { it == "data" } //filter the data based on predicate
        .subscribe { println(it) }

    Mono.just("data")
        .filterWhen { Mono.just(it == "data") } //filter the data based on predicate
        .subscribe { println(it) }
}

fun useOfMapAndFlatMap() {
    Mono.just("data")
        .map { it.uppercase() } //transform the data
        .flatMap { Mono.just(it) } //transform the data and return mono
        .flatMapIterable { it.toList() } //transform the data and return iterable of data elements; returns flux
        .subscribe { println(it) }
    Mono.just("data")
        .flatMapMany { Mono.just(it) } //Transform the signals emitted by this Mono into signal-specific Publishers, then forward the applicable Publisher's emissions into the returned Flux
        .subscribe()
    Mono.just("data")
        .flux() // convert mono into flux with the element
        .subscribe()
    Mono.justOrEmpty<String>(null)
        .mapNotNull { it } //map the data and filter out null values
        .switchIfEmpty(Mono.just("default")) //switch to another mono if empty
        .defaultIfEmpty("default") //default value if empty
        .subscribe { println(it) }
}

fun useOfHandle() {
    Mono.just("data")
        .handle {
                data,
                sink -> sink.next(data + " 1") //handle the data and emit the data
        } //handle the data and emit the data
        .subscribe { println(it) }
}

fun useOfHasElement() {
    Mono.just("data")
        .hasElement() //check if mono has element and return Mono<Boolean>
        .subscribe { println(it) }
}

fun useOfHide() {
    Mono.just("data")
        .hide() //hide the identity of the mono
        .subscribe { println(it) }
}

fun useOfIgnoreElelment() {
    Mono.just("data")
        .ignoreElement() //ignore the element and only propagate termination signal
        .subscribe()
}

fun useOfLog() {
    Mono.just("data")
        .log() //log the signals
        .subscribe()
}

fun useOfMergeWith() {
    Mono.just("data")
        .mergeWith(Mono.just("data2")) //merge the mono with another mono and return flux
        .subscribe { println(it) }
}

fun useOfName() {
    val mono = Mono.just("data")
        .name("Mono Name") //assign name to mono and can be retrieved by using Scannable.from(mono).name()

    println(Scannable.from(mono).name())
}

fun useOfOfType() {
    Mono.justOrEmpty<Int>(null)
        .ofType(Integer::class.java) //filter the data based on type; if not match then empty mono
        .subscribe { println(it) }
}

fun useOfOnError() {
    Mono.just("")
        .onErrorResume { Mono.just("data") } //resume the mono on error
        .onErrorReturn("data") //return the data on error
        .onErrorComplete() //complete the mono on error
        .onErrorMap { IllegalStateException() } //map the error to another error
        .onErrorContinue { throwable, data -> println("Error: $throwable, Data: $data") } //continue the mono on error
        .onErrorStop() //stop the mono on error
        .subscribe()
}

fun useOfOnTerminateDetach() {
    Mono.just("data")
        .onTerminateDetach() //returns a detachable mono, detach the termination or cancellation; help in non-reactor subscriber
        .subscribe { println(it) }
}

fun useOfOr() {
    Mono.just("data")
        .or(Mono.just("data2")) //return first available signal from mono or passed mono
        .subscribe { println(it) }
}

fun useOfPublish() {
    Mono.just("data")
        .doOnNext { println("doOnNext: $it") }
        .publish { input -> //share a mono (transform or consume many times without causing multiple subscription to upstream)
            input
                .map { it.length }
                .flatMap { input.map { it.length * 2 } }
        }
        .subscribe { println(it) }
}

fun useOfPublishOnAndSubscribeOn() {
    val mono1 = Mono.just("data1")
        .doOnSubscribe { println("mono1: $it") }
    val mono2 = mono1.publishOn(Schedulers.boundedElastic()) //creates a new mono, which will be subscribed after upstream onNext, onComplete, onError signals
        .map { it.length }.doOnSubscribe { println("mono2: $it") }
    mono2.subscribe { println("subscribed1: $it") }
    println("=====")
    val mono3 = mono1.subscribeOn(Schedulers.boundedElastic()) //creates a new mono, which will be subscribed after downstream onNext, onComplete, onError signals
        .map { it.length * 2 }.doOnSubscribe { println("mono3: $it") }
    mono3.subscribe { println("subscribed2: $it") }
    Thread.sleep(5000)
}

fun useOfRepeat() {
    val mono = Mono.just("data")
        .doOnSuccess { println("Mono is done") }
    mono
        .repeat() //repeat the mono indefinitely and return flux
        //.subscribe { println(it) }

    mono
        .repeat { false } // repeat if given predicate returns true
        .subscribe { println(it) }

    mono
        .repeat(5) //repeat the mono 5 times
        .subscribe { println("repeat5: $it") }

    mono
        .repeat(5) { false } //repeat the mono 5 times when given predicate resolves to true
        .subscribe { println("repeat5b: $it") }

    mono
        .repeat(5) { false } //repeat the mono 5 times when given predicate resolves to true
        .subscribe { println("repeat5c: $it") }

    mono
        .repeatWhen { it.delayElements(Duration.ofSeconds(1L)) } //Flux<Long> is provided as factory and repeat till factory emits signals
       //.subscribe { println("repeatWhen: $it") }

    mono
        .repeatWhenEmpty { it.delayElements(Duration.ofSeconds(1L)) } //Flux<Long> is provided as factory and repeat till factory emits signals when mono completes as empty
        .subscribe { println("repeatWhenEmpty: $it") }
}

fun useOfRetry() {
    val mono = Mono.just("data")
        .doOnSuccess { println("Mono is done") }
    mono
        .retry() //retry indefinitely if mono has any error
        .subscribe { println(it) }

    mono
        .retry(5) //retry 5 times if mono has any error
        .subscribe { println("retry5: $it") }

    mono
        .retryWhen(Retry.maxInARow(2)) //retry as per RetrySpec defined if mono has any error
        .subscribe { println("retryWhen: $it") }
}

fun useOfShare() {
    val mono = Mono.just("data")
        .doOnSuccess { println("Mono 1 is done") }
    mono
        .share() //prepare a mono which shares upstream mono result
        .doOnSuccess { println("Mono 2 is done") }
        .subscribe { println(it) }
}

fun useOfSingle() {
    val mono = Mono.just("data")

    mono
        .single() //return single element mono from mono; if empty throw NoSuchElementException
        .subscribe { println(it) }

    mono
        .flatMap { Mono.empty<String>() }
        .single()
        .onErrorResume {
            println("Error: ${it.message}")
            Mono.just("default")
        }
        .subscribe { println(it) }

    mono
        .singleOptional() //return single Optional mono from mono; if empty throw NoSuchElementException
        .subscribe { println(it.getOrNull()) }

    mono
        .flatMap { Mono.empty<String>() }
        .singleOptional()
        .subscribe { println(it.getOrNull()) }
}

fun useOfSubscribe() {
    val mono = Mono.just("data")

    mono.subscribe() //subscribe to mono
    mono.subscribe { println(it) } //subscribe to mono with given consumer
    mono.subscribe({ println(it) }) { it.printStackTrace() } //subscribe to mono with given consumer and error consumer
    mono.subscribe({ println(it) }, { it.printStackTrace() }) { println("Completed") } //subscribe to mono with given consumer, error consumer and completion consumer
    mono.subscribe({ println(it) }, { it.printStackTrace() }, { println("Completed") }) {
        it.request(1)
        it.cancel()
    } //subscribe to mono with given consumer, error consumer, completion consumer and subscription consumer
    val initialContext = Context.of("key", "value")
    mono.subscribe(
        { println(it) },
        { it.printStackTrace() },
        { println("Completed") },
        initialContext
    ) //subscribe to mono with given consumer, error consumer, completion consumer and initial context

    mono.subscribe(object: Subscriber<String> {
        override fun onSubscribe(s: Subscription?) {
            TODO("Not yet implemented")
        }

        override fun onError(t: Throwable?) {
            TODO("Not yet implemented")
        }

        override fun onComplete() {
            TODO("Not yet implemented")
        }

        override fun onNext(t: String?) {
            TODO("Not yet implemented")
        }
    })

    val subscribed = mono.subscribeWith(object: Subscriber<String> {
        override fun onSubscribe(s: Subscription?) {
            s?.request(1)
        }

        override fun onError(t: Throwable?) {
            println("Error: ${t?.message}")
        }

        override fun onComplete() {
            println("Completed")
        }

        override fun onNext(t: String?) {
            println(t)
        }
    }) //subscribe to mono with given subscriber
}

fun useOfTag() {
    val mono = Mono.just("data")
        .tag("tag1", "tag value") //tag the mono with given tag details
        .log()
    Scannable.from(mono).tags().forEach {
        println("key: ${it.t1}, value: ${it.t2}")
    }
}

fun useOfTake() {
    val mono = Mono.just("data")
        .doOnSubscribe { println("Subscribed 1") }

    mono.delayElement(Duration.ofSeconds(5))
        .take(Duration.ofSeconds(1)) //give this mono a chance to resolve within specified duration, but complete if it doesn't
        .subscribe { println("1: $it") }
    mono.delayElement(Duration.ofSeconds(5))
        .take(Duration.ofSeconds(1), Schedulers.boundedElastic()) //give this mono a chance to resolve within specified duration, but complete if it doesn't (on given scheduler)
        .subscribe { println("2: $it") }
    val anotherMono = Mono.empty<String>()
        .doOnSubscribe { println("Subscribed 2") }
    mono
        .takeUntilOther(anotherMono) //give this mono a chance to resolve before publisher emits
        .subscribe { println("3: $it") }
}

fun useOfTap() {
    val mono = Mono.just("data")

    val signalListener = object: SignalListener<String> {
        override fun doFirst() {
            println("SignalListener: doFirst")
        }

        override fun doFinally(terminationType: SignalType?) {
            println("SignalListener: doFinally: $terminationType")
        }

        override fun doOnSubscription() {
            println("SignalListener: doOnSubscription")
        }

        override fun doOnFusion(negotiatedFusion: Int) {
            println("SignalListener: doOnFusion: $negotiatedFusion")
        }

        override fun doOnRequest(requested: Long) {
            println("SignalListener: doOnRequest: $requested")
        }

        override fun doOnCancel() {
            println("SignalListener: doOnCancel")
        }

        override fun doOnComplete() {
            println("SignalListener: doOnComplete")
        }

        override fun doOnError(error: Throwable?) {
            println("SignalListener: doOnError: ${error?.message}")
        }

        override fun doAfterComplete() {
            println("SignalListener: doAfterComplete")
        }

        override fun doAfterError(error: Throwable?) {
            println("SignalListener: doAfterError: ${error?.message}")
        }

        override fun doOnMalformedOnError(error: Throwable?) {
            println("SignalListener: doOnMalformedOnError: ${error?.message}")
        }

        override fun doOnMalformedOnComplete() {
            println("SignalListener: doOnMalformedOnComplete")
        }

        override fun handleListenerError(listenerError: Throwable?) {
            println("SignalListener: handleListenerError: ${listenerError?.message}")
        }

        override fun doOnMalformedOnNext(value: String?) {
            println("SignalListener: doOnMalformedOnNext: $value")
        }

        override fun doOnNext(value: String?) {
            println("SignalListener: doOnNext: $value")
        }
    }

    mono
        .tap(Supplier<SignalListener<String>> { signalListener }) //tap the signals
        .subscribe { println(it) }
}

fun useOfThen() {
    val mono1 = Mono.just("data1")
    val mono2 = Mono.just("data2")

    mono1
        .then() //complete the mono1 and return empty mono
        .subscribe()

    mono1
        .then(mono2) //complete the mono1 and return mono2
        .subscribe { println(it) }

    mono1
        .thenEmpty(mono2.then()) //complete the mono1 and complete mono2 and then return Mono<Void>
        .subscribe { println(it) }

    mono1
        .thenMany(Flux.just("data3", "data4")) //complete the mono1 and return flux
        .subscribe { println(it) }

    mono1
        .thenReturn("data3") //complete the mono1 and return mono of given data
        .subscribe { println(it) }
}

fun useOfTimed() {
    val mono = Mono.just("data")
    val timedMono = mono.timed() //returned a timed mono with same data; this Timed can be used to get elapsed time, data and time of subscription
    val timedMonoOnScheduler = mono.timed(Schedulers.boundedElastic())
    timedMono
        .subscribe {
            println(it.elapsed())
            println(it.get())
            println(it.elapsedSinceSubscription())
        }
}

fun useOfTimeout() {
    val mono = Mono.just("data")
    val mono1 = mono.timeout(Duration.ofSeconds(1L)) //throws TimeoutException if mono doesn't emit signal within given duration
    val mono2 = mono.timeout(Duration.ofSeconds(1L), Mono.just("default")) //return default value if mono doesn't emit signal within given duration
    val mono3 = mono.timeout(Duration.ofSeconds(1L), Mono.just("default"), Schedulers.boundedElastic()) //return default value if mono doesn't emit signal within given duration on given scheduler
    val mono4 = mono.timeout(Duration.ofSeconds(1L), Schedulers.boundedElastic()) //throws TimeoutException if mono doesn't emit signal within given duration on given scheduler
    val mono5 = mono.timeout(Mono.just("default")) //throws TimeoutException if mono doesn't emit signal before given publisher emits
    val mono6 = mono.timeout(Mono.just("default"), Mono.just("fallback")) //return fallback value if mono doesn't emit signal before given publisher emits
}

fun useOfTimestamp() {
    val mono = Mono.just("data")
    val timestampedMono = mono.timestamp() //return a Tuple2 where t1 is timestamp and t2 is data
    val timestampedMonoOnScheduler = mono.timestamp(Schedulers.boundedElastic())
    timestampedMono
        .subscribe {
            println(it.t1)
            println(it.t2)
        }
}

fun useOfTransform() {
    val mono = Mono.just("data")
    val transformedMono = mono.transform { //transform the mono with given transformer to generate target mono
        it.map { str -> str.length * 2 }
    }
    transformedMono.subscribe { println(it) }
    val transformedMono2 = mono.transformDeferred {
        it.map { str -> str.length * 2 }
    }
    val transformedMono3 = mono.transformDeferredContextual { data, context ->
        data.map { str -> str.length * 2 }
    }
}

fun useOfZipWhen() {
    val mono = Mono.just("data")
    mono.zipWhen { Mono.just("data2") } //wait for result of this mono; use it to create a new mono with passed mono using right generator
        .subscribe { println(it) }

    mono.zipWhen ({ Mono.just("data2") }) { main, passed -> main }  //wait for result of this mono; use it to create a new mono with passed mono using right generator and BiFunction combinator
}

fun useOfZipWith() {
    val mono = Mono.just("data")
    mono.zipWith(Mono.just("data2")) //combine result the mono with another mono and return tuple
        .subscribe { println(it) }

    mono.zipWith(Mono.just("data2")) { main, passed -> main } //combine result the mono with another mono and combined BinFunction combinator and return tuple
}
