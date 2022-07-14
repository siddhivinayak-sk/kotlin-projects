package com.sk.project4

import com.sk.project4.configuration.ContractTestInitializer
import com.sk.project4.configuration.MongodbTestInitializer
import com.sk.project4.handler.PersonHandler
import com.sk.project4.service.PersonService
import io.restassured.module.webtestclient.RestAssuredWebTestClient
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.test.web.reactive.server.WebTestClientBuilderCustomizer
import org.springframework.cache.CacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.utility.DockerImageName
import java.util.*

@AppFluxTest
@ActiveProfiles("test")
open class Project4ApplicationTests {

	@Autowired private var contractTestInitializers: MutableList<ContractTestInitializer>? = mutableListOf()
	@Autowired private lateinit var webTestClient: WebTestClient
	@Autowired private var cacheManager: CacheManager? = null
	private var encoder = Base64.getEncoder();

	//@Container
	//val mongoDBContainer: MongoDBContainer = MongoDBContainer(DockerImageName.parse("mongo:4.0.10")).withExposedPorts(27017)

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
		//mongoDBContainer.start()
	}

	@AfterEach
	fun afterEach() {
		contractTestInitializers?.forEach { it.cleanUpTest() }
		//mongoDBContainer.stop()
	}

	//@Test
	fun contextLoads() {
	}

	fun basicAuth(cred: String): String {
		return "Basic " + encoder.encodeToString(cred.toByteArray())
	}

}