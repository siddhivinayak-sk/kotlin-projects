package com.sk.project4.client

import com.sk.project4.model.entity.Person
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

/*
fun main(args:Array<String>) {
    //var client: PersonClient = PersonClient("http://127.0.0.1:8080/api")
    //client.get(101)

    Mono.empty<Void>()
        .map { returnUnit() }
        .doOnSubscribe {
            println("do on success")
        }
        .doOnSubscribe {
            println("do on subscribe")
        }

        .subscribe()
}
 */

fun returnUnit(): Unit {}

class PersonClient {
    private var baseUri: String
    private var webClient: WebClient

    constructor(uri: String) {
        baseUri = uri
        webClient = WebClient.create(baseUri)
    }

    fun get(id: Long) {
        var person: Mono<Person> = webClient
            .get()
            .uri("/person/" + id)
            .headers { headers -> headers.setBasicAuth("admin", "adminpwd") }
            .accept(MediaType.APPLICATION_JSON)
            .attribute("Content-Type", MediaType.APPLICATION_JSON)
            .retrieve()
            .bodyToMono(Person::class.java)
        println(person.block()?.name)
    }

    fun getAll() {
        var person: Flux<Person> = webClient
            .get()
            .uri("/person")
            .headers { headers -> headers.setBasicAuth("admin", "adminpwd") }
            .retrieve()
            .bodyToFlux(Person::class.java)
            person.subscribe(System.out::println)
    }

    fun findByName(name: String) {
        var person: Flux<Person> = webClient
            .get()
            .uri { uriBuilder -> uriBuilder.path("/person/search").queryParam("name", name).build() }
            .headers { headers -> headers.setBasicAuth("admin", "adminpwd") }
            .retrieve()
            .bodyToFlux(Person::class.java)
        person.subscribe(System.out::println)
    }

    fun create(id: Long) {
        var person: Mono<Person> = webClient
            .post()
            .uri("/person")
            .headers { headers -> headers.setBasicAuth("admin", "adminpwd") }
            .accept(MediaType.APPLICATION_JSON)
            .attribute("Content-Type", MediaType.APPLICATION_JSON)
            .body(Mono.just(Person(id, "User-" + id, "Pass" + id)), Person::class.java)
            .retrieve()
            .bodyToMono(Person::class.java)
        println(person.block())
    }

    fun update(id: Long) {
        var person: Mono<Person> = webClient
            .put()
            .uri("/person/" + id)
            .headers { headers -> headers.setBasicAuth("admin", "adminpwd") }
            .body(Mono.just(Person(id, "UX102Z-" + id, "PX102Z-" + id)), Person::class.java)
            .retrieve()
            .bodyToMono(Person::class.java)
        println(person.block())
    }

    fun delete(id: Long) {
        var person: Mono<Void> = webClient
            .delete()
            .uri("/person/" + id)
            .headers { headers -> headers.setBasicAuth("admin", "adminpwd") }
            .retrieve()
            .bodyToMono(Void::class.java)
        person.block()
    }
}