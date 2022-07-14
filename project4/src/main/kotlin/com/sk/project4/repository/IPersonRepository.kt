package com.sk.project4.repository

import com.sk.project4.model.entity.Person
import reactor.core.publisher.Flux

interface IPersonRepository {

    fun getAllPersonStartedWith(str: String): Flux<Person>

    fun updatePassword(name: String, password: String): Long

}