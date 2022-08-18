package com.sk.project6.resource

import org.springframework.http.MediaType
import org.springframework.http.codec.ServerSentEvent
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter.SseEventBuilder
import reactor.core.publisher.Flux
import java.time.Duration
import java.time.LocalTime
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


@RestController
@RequestMapping("/box")
class SSEResource {

    data class EventData(val time: String, val id: String)

    @GetMapping("/stream-sse", produces = [ MediaType.TEXT_EVENT_STREAM_VALUE ])
    fun streamEvent(): Flux<ServerSentEvent<EventData>> {
        return Flux.interval(Duration.ofSeconds(1))
            .map {
                val event = if(it.toInt() % 2 == 0) "message" else "notice"
                ServerSentEvent.builder<EventData>()
                .id(it.toString())
                .event(event)
                .data(EventData("SSE - " + LocalTime.now().toString(), it.toString()))
                .build()
            }
    }

    @GetMapping("/stream-sse-mvc", produces = [ MediaType.TEXT_EVENT_STREAM_VALUE ])
    fun streamSseMvc(): SseEmitter {
        var emitter: SseEmitter = SseEmitter(15000)
        var sseMvcExecutor: ExecutorService = Executors.newSingleThreadExecutor()

        sseMvcExecutor.execute {
            try {
                var i: Int = 0
                while(true) {
                    i++

                    var event: SseEventBuilder = SseEmitter
                        .event()
                        .data("SSE MVC - " + LocalTime.now().toString())
                        .id(i.toString())
                        .name("message1")

                    emitter.send(event)

                    Thread.sleep(10000)
                }
            }
            catch (ex: Throwable) {
                emitter.completeWithError(ex)
                ex.printStackTrace()
            }
        }
        return emitter
    }


    private val emitters: MutableList<SseEmitter> = ArrayList()

    @GetMapping("/listen")
    fun getEvents(): SseEmitter? {
        val emitter = SseEmitter()
        emitter.onCompletion { println("OnCompletion fired") }
        emitter.onError { println("OnError Fired ${it.printStackTrace()}") }
        emitter.onTimeout { println("OnTimeout fired") }
        emitters.add(emitter)
        emitter.onCompletion { emitters.remove(emitter) }
        return emitter
    }

    @PostMapping("/notify")
    fun postMessage(message: Any) {
        for (emitter in emitters) {
            emitter.send(message)
        }
    }

}