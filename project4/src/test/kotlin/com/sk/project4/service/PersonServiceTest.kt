@file: Suppress("ClassName")

package com.sk.project4.service

import com.sk.project4.AppTest
import com.sk.project4.model.entity.Person
import com.sk.project4.repository.PersonRepository
import org.amshove.kluent.*
import io.mockk.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

@AppTest
class PersonServiceTest {

    private val mockPersonRepository = mockk<PersonRepository>(relaxUnitFun = true)

    @Nested
    inner class `person by id` {
        private val personService: PersonService = PersonService(mockPersonRepository)

        private val emptyId: Long = 100
        private val inputId: Long = 101
        private val person = Person(inputId, "Abc", "Xyz")
        private val output = Mono.just(person)

        @Test
        fun `should return person mono` () {
            every { mockPersonRepository.findById(inputId) } returns output
            val outputPerson = personService.get(inputId)
            outputPerson `should be` output
        }

        @Test
        fun `should return person` () {
            every { mockPersonRepository.findById(inputId) } returns output
            val outputPerson = personService.get(inputId)
            outputPerson.subscribe {
                it.id `should be` inputId
                it.name `should not be` ""
            }
        }

        @Test
        fun `should return person with step verifier` () {
            every { mockPersonRepository.findById(inputId) } returns output
            StepVerifier
                .create(personService.get(inputId))
                .expectSubscription()
                .consumeNextWith {
                    it `should not be` null
                    it.id `should be` inputId
                }
                .verifyComplete()
        }

        @Test
        fun `should return empty person with step verifier` () {
            every { mockPersonRepository.findById(emptyId) } returns Mono.empty()
            StepVerifier
                .create(personService.get(emptyId))
                .expectSubscription()
                .verifyComplete()
        }
    }
}