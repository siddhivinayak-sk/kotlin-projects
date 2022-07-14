package com.sk.project6

import io.restassured.module.webtestclient.RestAssuredWebTestClient
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.test.web.reactive.server.WebTestClientBuilderCustomizer
import org.springframework.cache.CacheManager
import org.springframework.context.annotation.Bean
import org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation
import org.springframework.test.web.reactive.server.WebTestClient
import java.util.*

@AppFluxTest
open class Project6ApplicationTests {

    @Autowired private var contractTestInitializers: MutableList<ContractTestInitializer>? = mutableListOf()
    @Autowired private lateinit var webTestClient: WebTestClient
    @Autowired private var cacheManager: CacheManager? = null
    private var encoder = Base64.getEncoder();

    @TestConfiguration
    inner class Config {

        @Bean
        fun restDocsParameterizedOutput(): WebTestClientBuilderCustomizer {
            return WebTestClientBuilderCustomizer { builder ->
                builder.entityExchangeResultConsumer(WebTestClientRestDocumentation.document("{class-name}/{method-name}"))
            }
        }
    }

    @BeforeEach
    fun beforeEach() {
        RestAssuredWebTestClient.webTestClient(webTestClient)
        contractTestInitializers?.forEach { it.initTest() }
        cacheManager?.run { cacheNames.mapNotNull { getCache(it) }.forEach { it.clear() } }
    }

    @AfterEach
    fun afterEach() {
        contractTestInitializers?.forEach { it.cleanUpTest() }
    }

    @Test
    fun contextLoads() {
    }

    fun basicAuth(cred: String): String {
        return "Basic " + encoder.encodeToString(cred.toByteArray())
    }
}