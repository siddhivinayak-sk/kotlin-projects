package com.sk.project4.service

import com.sk.project4.model.entity.Person
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface IPersonService {
    fun get(id: Long): Mono<Person>

    fun getAll(): Flux<Person>

    fun findByName(name: String): Flux<Person>

    fun create(person: Mono<Person>): Mono<Person>

    fun update(id: Long, person: Mono<Person>): Mono<Person>

    fun delete(id: Long): Mono<Void>

    fun count(): Mono<Long>
}