package com.sk.project4

import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.test.autoconfigure.OverrideAutoConfiguration
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTypeExcludeFilter
import org.springframework.boot.test.autoconfigure.filter.TypeExcludeFilters
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.autoconfigure.web.reactive.WebTestClientAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Import
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import java.lang.annotation.*
import java.lang.annotation.Retention
import java.lang.annotation.Target

@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@WebFluxTest
@AutoConfigureWebClient
@AutoConfigureRestDocs
@ComponentScan("com.sk.project4")
@AutoConfigureDataMongo
@TestPropertySource(properties = ["spring.mongodb.embedded.version=3.5.5"])
@ExtendWith(value = [RestDocumentationExtension::class, SpringExtension::class])

//@ExtendWith(SpringExtension::class)
//@DataMongoTest
//@OverrideAutoConfiguration(enabled = false)
//@TypeExcludeFilters(DataMongoTypeExcludeFilter::class)
//@AutoConfigureCache
//@ImportAutoConfiguration

annotation class AppFluxTest()
