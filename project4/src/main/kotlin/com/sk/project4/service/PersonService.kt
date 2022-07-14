package com.sk.project4.service

import com.sk.project4.model.entity.Person
import com.sk.project4.repository.PersonRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class PersonService: IPersonService {

    lateinit var personRepository: PersonRepository

    @Autowired
    constructor(personRepository: PersonRepository) {
        this.personRepository = personRepository
    }

    override fun get(id: Long): Mono<Person> {
        return personRepository.findById(id)
    }

    override fun getAll(): Flux<Person> {
        return personRepository.findAll()
    }

    override fun findByName(name: String): Flux<Person> {
        return personRepository.findByName(name)
    }

    override fun create(person: Mono<Person>): Mono<Person> {
        return person.flatMap(personRepository::save)
    }

    override fun update(id: Long, person: Mono<Person>): Mono<Person> {
        return personRepository
            .findById(id)
            .map {it ->
                    val p: Person? = person.block()
                    p?.let {
                        it.name = p.name
                        it.password = p.password
                    }
                return@map it
                }.flatMap(personRepository::save)
    }

    override fun delete(id: Long): Mono<Void> {
        return personRepository.deleteById(id)
    }

    override fun count(): Mono<Long> {
        return personRepository.count()
    }
}