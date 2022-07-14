package com.sk.project6.dao

import com.sk.project6.model.Country
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.Optional

@Repository
class CountryDao: ICountryDao {
    private val countries = mutableListOf<Country>()
    private val zero: Long = 0

    init {
        countries.add(Country(1, "India", "IN"))
        countries.add(Country(2, "United States", "US"))
        countries.add(Country(3, "China", "CI"))
        countries.add(Country(4, "Canada", "CD"))
        countries.add(Country(5, "Britain", "BR"))
    }

    private fun save(country: Country): Mono<Country> {
        countries.add(country)
        return Mono.just(country)
    }

    private fun modifyAndSave(country: Country): Mono<Country> {
        countries.stream().filter{it.id == country.id }.map {
            it.name = country.name
            it.code = country.code
        }
        return Mono.just(country)
    }

    override fun create(country: Country): Mono<Country> {
        return if (countries.stream().filter{ it.id == country.id }.count() > zero)
            Mono.error(Exception("Record Already Exists")) else save(country)
    }

    override fun update(country: Country): Mono<Country> {
        return if (countries.stream().filter{ it.id == country.id }.count() == zero)
            Mono.error(Exception("Record Already Exists")) else modifyAndSave(country)
    }

    override fun delete(country: Long): Mono<Country> {
        var tmp: Optional<Country> = countries.stream().filter{ it.id == country }.findAny()
        tmp.ifPresent { countries.remove(it) }
        return Mono.just(tmp.orElseGet { null })
    }

    override fun get(country: Long): Mono<Country> {
        var tmp: Optional<Country> = countries.stream().filter{ it.id == country }.findAny()
        return tmp.map { Mono.just(it) }.orElseGet { Mono.empty() }
    }

    override fun getAll(): Flux<Country> {
        return Flux.fromIterable(countries)
    }
}