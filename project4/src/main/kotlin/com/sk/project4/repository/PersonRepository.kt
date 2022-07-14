package com.sk.project4.repository

import com.sk.project4.model.entity.Person
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux

@Repository
interface PersonRepository: ReactiveMongoRepository<Person, Long>, IPersonRepository {

    fun findByName(name: String): Flux<Person>

}