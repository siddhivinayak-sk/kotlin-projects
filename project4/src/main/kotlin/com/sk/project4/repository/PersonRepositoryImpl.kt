package com.sk.project4.repository

import com.mongodb.client.result.UpdateResult
import com.sk.project4.model.entity.Person
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
class PersonRepositoryImpl: IPersonRepository {

    lateinit var template: ReactiveMongoTemplate

    @Autowired
    constructor(template: ReactiveMongoTemplate) {
        this.template = template
    }

    override fun getAllPersonStartedWith(str: String): Flux<Person> {
        val query: Query = Query(Criteria.where("name").regex(str))
        return template.find(query, Person::class.java)
    }

    override fun updatePassword(name: String, password: String): Long {
        val query: Query = Query(Criteria.where("name").`is`(name))
        val update: Update = Update()
        update.set("password", password)
        val result: Mono<UpdateResult> = template.updateFirst(query, update, Person::class.java)
        return result.map { it.matchedCount }.block() ?: 0
    }
}