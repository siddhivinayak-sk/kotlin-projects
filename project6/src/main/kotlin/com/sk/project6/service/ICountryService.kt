package com.sk.project6.service

import com.sk.project6.model.Country
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface ICountryService {
    fun create(country: Country): Mono<Country>
    fun update(country: Country): Mono<Country>
    fun delete(country: Long): Mono<Country>
    fun get(country: Long): Mono<Country>
    fun getAll(): Flux<Country>
}