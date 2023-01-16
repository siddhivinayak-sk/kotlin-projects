package com.sk.project9

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.SpringApplication
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.context.ConfigurableApplicationContext
import java.time.Duration

@SpringBootTest
class Project9ApplicationTests {

	@Test
	fun contextLoads() {
	}

	@Autowired lateinit var configurableApplicationContext: ConfigurableApplicationContext
	@Autowired lateinit var applicationContext: ApplicationContext

	@Test
	fun contextTest() {
		var springApplication = SpringApplication()
		var event = ApplicationReadyEvent(springApplication, emptyArray(), configurableApplicationContext, Duration.ofSeconds(100))
		var listener: CustomApplicationListener = CustomApplicationListener()
		listener.onApplicationEvent(event)
	}
}


