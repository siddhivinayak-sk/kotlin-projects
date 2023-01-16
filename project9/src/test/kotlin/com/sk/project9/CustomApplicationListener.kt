package com.sk.project9

import org.apache.logging.log4j.LogManager
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.ApplicationListener

class CustomApplicationListener: ApplicationListener<ApplicationReadyEvent> {

    val logger = LogManager.getLogger()

    override fun onApplicationEvent(event: ApplicationReadyEvent) {
        logger.info("ApplicationName: ${event.applicationContext.applicationName}")
    }
}