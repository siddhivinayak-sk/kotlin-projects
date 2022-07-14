package com.sk.project4

import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient
import org.springframework.boot.test.autoconfigure.web.reactive.WebTestClientAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import java.lang.annotation.*
import java.lang.annotation.Retention
import java.lang.annotation.Target

@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest
@AutoConfigureWebClient
@AutoConfigureRestDocs
@TestPropertySource(properties = ["spring.mongodb.embedded.version=3.5.5"])

annotation class AppTest()
