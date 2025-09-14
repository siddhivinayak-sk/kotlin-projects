package com.sk.ai.content

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.ai.vectorstore.VectorStore
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.ApplicationListener
import org.springframework.stereotype.Component

@Component
class ContentLoader(
        private val contentProcessors: List<ContentProcessor>,
        private val vectorStore: VectorStore?,
): ApplicationListener<ApplicationReadyEvent> {

    val logger: Logger = LoggerFactory.getLogger(ContentLoader::class.java)

    override fun onApplicationEvent(event: ApplicationReadyEvent) {
        logger.info("Starting content loading...")
        var count = 0
        contentProcessors.forEach { processor ->
            logger.info("Loading content from processor: ${processor::class.simpleName}")
            val docs = processor.get()
            logger.info("Found ${docs.size} processors docs")
            count += docs.size
            vectorStore?.let { vs -> docs.forEach { vs.add(listOf(it)) } }
            logger.info("Content loaded from processor: ${processor::class.simpleName}")
        }
        logger.info("Content loading completed. Total documents loaded: $count")
    }
}
