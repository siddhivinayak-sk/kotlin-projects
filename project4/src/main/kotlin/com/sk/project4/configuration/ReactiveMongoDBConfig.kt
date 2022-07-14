package com.sk.project4.configuration

import com.mongodb.reactivestreams.client.MongoClient
import com.mongodb.reactivestreams.client.MongoClients
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories

@Configuration
@EnableReactiveMongoRepositories("com.sk.project4.repository")
class ReactiveMongoDBConfig {

    fun mongoClient(): MongoClient {
        return MongoClients.create();
    }

    fun getDatabaseName(): String {
        return "reactive"
    }

    fun reactiveMongoTemplate(): ReactiveMongoTemplate  {
        return ReactiveMongoTemplate(mongoClient(), getDatabaseName())
    }
}