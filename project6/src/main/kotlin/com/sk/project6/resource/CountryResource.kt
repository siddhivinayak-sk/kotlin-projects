package com.sk.project6.resource

import com.sk.project6.model.Country
import com.sk.project6.service.CountryService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping(ResourceConstants.API_PATH)
class CountryResource {

    private lateinit var countryService: CountryService

    @Autowired
    fun setCountryService(countryService: CountryService) {
        this.countryService = countryService
    }

    @GetMapping("/{id}")
    fun get(@PathVariable("id") id: Long): Mono<Country> {
        return countryService.get(id)
    }

    @GetMapping
    fun get(): Flux<Country> {
        return countryService.getAll()
    }

    @PostMapping
    fun create(@RequestBody country: Country): Mono<Country> {
        return countryService.create(country)
    }

    @PutMapping
    fun update(@RequestBody country: Country): Mono<Country> {
        return countryService.update(country)
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable("id") id: Long): Mono<Country> {
        return countryService.delete(id)
    }

}