package com.sk.project7.handler

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.web.reactive.socket.WebSocketHandler
import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.Duration
import com.sk.project7.model.Event
import com.sk.project7.model.Score
import com.sk.project7.util.ScoreEventListener
import com.sk.project7.util.ScoreManager
import io.swagger.v3.core.util.Json
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import reactor.core.publisher.FluxSink
import reactor.core.publisher.SynchronousSink
import java.time.Instant
import java.util.*

@Component("ReactiveWebSocketHandler")
class ReactiveWebSocketHandler: WebSocketHandler {

    val eventFlux: Flux<String> = Flux.generate{sink ->
        val event: Event = Event(UUID.randomUUID().toString(), Instant.now().toString())
        try {
            sink.next(json.writeValueAsString(event));
        } catch (e: JsonProcessingException) {
            sink.error(e);
        }
    }

    val json: ObjectMapper = ObjectMapper()
    val intervalFlux: Flux<String> = Flux.interval(Duration.ofMillis(1000L)).zipWith(eventFlux, {time, event -> event})

    @Autowired lateinit var scoreManager: ScoreManager

    val scoreFluxWithEvent: Flux<String> = Flux.create { sink ->
        scoreManager.registerListener(object : ScoreEventListener {
            override fun scoreChange(score: Score) {
                sinkNext(sink)
            }
        })
    }

    val scoreFluxWithTime: Flux<String> = Flux.interval(Duration.ofMillis(1000L)).map { scoreManager.currentScore().toData() }

    override fun handle(session: WebSocketSession): Mono<Void> {
        return session.send(
            //intervalFlux //For generated event with time based Flux element
            //scoreFluxWithEvent //For score with event generated by controller
            scoreFluxWithTime //For score with time based Flux element
                .map(session::textMessage))

            .and(session.receive().map{ msg ->
                msg.payloadAsText
            }.log())
    }

    private fun sinkNext(sink: FluxSink<String>) {
        sink.next(scoreManager.currentScore().toData())
    }

    private fun sinkNext(sink: SynchronousSink<String>) {
        sink.next(scoreManager.currentScore().toData())
    }

    private fun Score.toData(): String {
        val str = try {
            json.writeValueAsString(this)
        }
        catch (e: JsonProcessingException) {
            e.printStackTrace()
            ""
        }
        return str
    }
}