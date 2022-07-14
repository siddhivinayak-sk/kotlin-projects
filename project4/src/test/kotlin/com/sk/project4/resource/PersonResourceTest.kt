@file: Suppress("Classname")
package com.sk.project4.resource

import com.mongodb.reactivestreams.client.MongoClients
import com.sk.project4.AppFluxTest
import com.sk.project4.AppTest
import com.sk.project4.model.entity.Person
import com.sk.project4.repository.PersonRepository
import com.sk.project4.service.PersonService

//Enable it for embeded mongo db
//import de.flapdoodle.embed.mongo.MongodExecutable
//import de.flapdoodle.embed.mongo.MongodStarter
//import de.flapdoodle.embed.mongo.config.ImmutableMongodConfig
//import de.flapdoodle.embed.mongo.config.MongodConfig
//import de.flapdoodle.embed.mongo.config.Net
//import de.flapdoodle.embed.mongo.distribution.Version
//import de.flapdoodle.embed.process.runtime.Network

import io.mockk.every
import io.mockk.mockk
import org.amshove.kluent.`should be`
import org.amshove.kluent.`should not be`
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebTestClientAutoConfiguration
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.test.web.reactive.server.WebTestClientBuilderCustomizer
import org.springframework.cloud.contract.wiremock.restdocs.SpringCloudContractRestDocs
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.http.MediaType
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation
import org.springframework.restdocs.webtestclient.WebTestClientRestDocumentationConfigurer
import org.springframework.stereotype.Component
import org.springframework.test.context.TestPropertySource
import org.springframework.test.web.reactive.server.EntityExchangeResult
import org.springframework.test.web.reactive.server.ExchangeResult
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.utility.DockerImageName
import reactor.core.publisher.Mono
import kotlin.test.assertNotNull

@AppFluxTest
class PersonResourceTest {

    @Container
    val mongoDBContainer: MongoDBContainer = MongoDBContainer(DockerImageName.parse("mongo:4.0.10")).withExposedPorts(27017)


    @Autowired
    private lateinit var webTestClient: WebTestClient

    //Enable for embeded mongo db
    //private var mongodExecutable: MongodExecutable? = null

    @AfterEach
    fun afterEach() {
        //Enable for embeded mongo db
        //mongodExecutable?.stop()

        mongoDBContainer.stop()
    }

    @BeforeEach
    fun beforeEach() {
        //Enable for embeded mongo db
        //mongodExecutable?.stop()
        //mongodExecutable?.start()

        mongoDBContainer.start()
    }

    @TestConfiguration
    inner class Config {

        @Bean
        fun restDocsParameterizedOutput(): WebTestClientBuilderCustomizer {
            return WebTestClientBuilderCustomizer {
                builder -> builder.entityExchangeResultConsumer(WebTestClientRestDocumentation.document("{class-name}/{method-name}"))
            }
        }

        //Enable for embeded mongo db
//        @Bean
//        fun mongoTemplate(): ReactiveMongoTemplate {
//            val ip: String = "localhost"
//            val port: Int = 27017
//
//            val mongodConfig: ImmutableMongodConfig = MongodConfig
//                .builder()
//                .version(Version.Main.PRODUCTION)
//                .net(Net(ip, port, Network.localhostIsIPv6()))
//                .build();
//
//            val starter: MongodStarter = MongodStarter.getDefaultInstance()
//            mongodExecutable = starter.prepare(mongodConfig)
//            mongodExecutable?.start()
//            return ReactiveMongoTemplate(MongoClients.create(String.format("mongodb://%s:%d", ip, port)), "reactive")
//        }
    }

    //@BeforeEach  //Enable it to configure WebTestClient manually
    fun setup(@Autowired context: ApplicationContext,
              @Autowired configurer: WebTestClientRestDocumentationConfigurer) {
        webTestClient = WebTestClient
            .bindToApplicationContext(context)
            .configureClient()
            .filter(configurer)
            .entityExchangeResultConsumer(WebTestClientRestDocumentation.document("{class-name}/{method-name}"))
            .build()
    }

    @Nested
    inner class `get person by id` {
        //Enable Mock Repository
        var personRepository = mockk<PersonRepository>(relaxUnitFun = true)
        @Autowired
        lateinit var personService: PersonService

        //Enable Mock service
        //var personService = mockk<PersonService>(relaxUnitFun = true)
        //@Autowired
        //lateinit var personResource: PersonResource

        var personId: Long = 201
        var person = Person(personId, "Abc", "Def")
        var monoPerson = Mono.just(person)

        val getByIdUri = "/{id}"


        @Test
        fun `should return person unauthorized without token`() {
            webTestClient
                .get()
                .uri {it.path(ResourceConstraints.PERSON_API + getByIdUri).build(personId)}
                .accept(MediaType.APPLICATION_JSON)
                .attribute("Content-Type", MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isUnauthorized
        }

        @Test
        fun `should return person`() {
            //Enable Mock service
            //every { personService.get(personId) } returns monoPerson
            //personResource.service = personService

            //Enable Mock Repository
            every { personRepository.findById(personId) } returns monoPerson
            personService.personRepository = personRepository

            webTestClient
                .get()
                .uri {it.path(ResourceConstraints.PERSON_API + getByIdUri).build(personId)}
                .headers { headers -> headers.setBasicAuth("admin", "adminpwd") }
                .accept(MediaType.APPLICATION_JSON)
                .attribute("Content-Type", MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                //.expectBody(Person::class.java)
                .expectBody()
                .consumeWith {
                    it `should not be` null
//                    val result: Person? = it.responseBody
//                    assertNotNull(result)
//                    result.id `should be` personId
                }
                .consumeWith(WebTestClientRestDocumentation.document("getbyid", SpringCloudContractRestDocs.dslContract()))
        }
    }
}