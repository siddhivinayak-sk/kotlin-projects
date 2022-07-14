package com.sk.project4.resource

import com.sk.project4.model.entity.Person
import com.sk.project4.service.PersonService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping(ResourceConstraints.PERSON_API)
class PersonResource {

    lateinit var service: PersonService

    @Autowired
    constructor(personService: PersonService) {
        service = personService
    }

    @GetMapping("/{id}")
    fun getPersion(@PathVariable id: Long): Mono<ResponseEntity<Person>> {
        return service
            .get(id)
            .map { ResponseEntity.ok(it) }
            .defaultIfEmpty(ResponseEntity.notFound().build())
    }

    @PostMapping
    fun addNewPerson(@RequestBody person: Person): Mono<Person> {
        return service.create(Mono.just(person));
    }

    @GetMapping
    fun getAllPerson(): Flux<Person> {
        return service.getAll()
    }

    @PutMapping("/{id}")
    fun  updatePerson(@PathVariable id: Long, @RequestBody person: Person): Mono<ResponseEntity<Person>> {
        return service
            .update(id, Mono.just(person))
            .map{ResponseEntity.ok(it) }
            .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    fun deletePerson(@PathVariable id: Long): Mono<ResponseEntity<Void>> {
        return service
            .delete(id)
            .map{ResponseEntity.ok(it) }
            .defaultIfEmpty(ResponseEntity.notFound().build());
    }

}