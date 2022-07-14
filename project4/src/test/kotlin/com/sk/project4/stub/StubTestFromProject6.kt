package com.sk.project4.stub

import com.sk.project4.AppFluxTest
import com.sk.project6.model.Country
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerPort
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties
import org.springframework.context.annotation.ComponentScan
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.client.RestTemplate
import java.net.http.HttpResponse
import java.util.*


@AppFluxTest
@AutoConfigureStubRunner(ids =
    ["com.sk:project6:0.0.1-SNAPSHOT:stubs"],
    //["com.sk:project6:0.0.1-SNAPSHOT:stubs:8085"],
    //["com.sk:project6:+:8085"],
    stubsMode = StubRunnerProperties.StubsMode.CLASSPATH)
class StubTestFromProject6 {

    //@Autowired
    private lateinit var webTestClient: WebTestClient

    @StubRunnerPort("project6") lateinit var port: Integer

    @BeforeEach
    fun beforeEach() {
        webTestClient = WebTestClient
            .bindToServer()
            .baseUrl("http://localhost:$port")
            .build()
    }

    @Test
    fun `get country from service contract`() {
        /*
        //Consuming wiremock endpoint with RestTemplate
        var rt: RestTemplate = RestTemplate()
        var headers = HttpHeaders()
        headers.setContentType(MediaType.APPLICATION_JSON)
        headers.setAccept(Collections.singletonList(MediaType.ALL))
        var httpEntity = HttpEntity<Void>(headers)
        val httpResponse: ResponseEntity<String> = rt.exchange("http://localhost:8085/api/country/1", HttpMethod.GET, httpEntity, String::class.java)
        println("This is result: ${httpResponse.statusCode}")
        */

        webTestClient.get()
            .uri("/api/country/1")
            .headers { headers -> headers.setBasicAuth("admin", "adminpwd") }
            .headers { it.contentType = MediaType.APPLICATION_JSON }
            .accept(MediaType.ALL)
            .exchange()
            .expectStatus().isOk()
            .expectBody(Country::class.java)
            .consumeWith {
                println(it.responseBody?.name)
            }

    }

}