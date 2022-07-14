package com.sk.project4.handler

import com.sk.project4.model.entity.Person
import com.sk.project4.service.PersonService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.lang.Long

@Component
class PersonHandler {

    lateinit var service: PersonService

    @Autowired
    constructor(service: PersonService) {
        this.service = service
    }

    fun getPerson(request: ServerRequest): Mono<ServerResponse> {
        return service
            .get(Long.parseLong(request.pathVariable("id")))
            .flatMap { person ->
                ServerResponse
                    .ok()
                    .body(BodyInserters.fromValue(person))
                    .switchIfEmpty(ServerResponse.notFound().build())
            };
    }

    fun getAllPerson(request: ServerRequest): Flux<ServerResponse> {
        return service
            .getAll()
            .flatMap { person ->
                ServerResponse
                    .ok()
                    .body(BodyInserters.fromValue(person))
                    .switchIfEmpty(ServerResponse.notFound().build())
            };
    }

    fun findByName(request: ServerRequest): Flux<ServerResponse> {
        return service
            .findByName(request.pathVariable("name"))
            .flatMap { person ->
                ServerResponse
                    .ok()
                    .body(BodyInserters.fromValue(person))
                    .switchIfEmpty(ServerResponse.notFound().build())
            };
    }

    fun createPerson(request: ServerRequest): Mono<ServerResponse> {
        val mPerson: Mono<Person> = request.bodyToMono(Person::class.java);
        return service
            .create(mPerson)
            .flatMap { person ->
                ServerResponse
                    .ok()
                    .body(BodyInserters.fromValue(person))
                    .switchIfEmpty(ServerResponse.notFound().build())
            };
    }

    fun updatePerson(request: ServerRequest): Mono<ServerResponse> {
        val id = Long.parseLong(request.pathVariable("id"))
        val mPerson: Mono<Person> = request.bodyToMono(Person::class.java);
        return service
            .update(id, mPerson)
            .flatMap { person ->
                ServerResponse
                    .ok()
                    .body(BodyInserters.fromValue(person))
                    .switchIfEmpty(ServerResponse.notFound().build())
            };
    }

    fun deletePerson(request: ServerRequest): Mono<ServerResponse> {
        val id = Long.parseLong(request.pathVariable("id"));
        return service
            .delete(id)
            .flatMap { person ->
                ServerResponse
                    .ok()
                    .body(BodyInserters.fromValue(person))
                    .switchIfEmpty(ServerResponse.notFound().build())
            };
    }



//    val validator: Validator = PersonValidator();
//
//    fun validate(person: Person) {
//        var errors: Errors = BeanPropertyBindingResult(person, "person")
//        validator.validate(person, errors)
//        if (errors.hasErrors()) {
//            throw ServerWebInputException(errors.toString())
//        }
//    }

}