package com.sk.ai.content.html

import com.sk.ai.config.properties.ContentProperties
import com.sk.ai.config.properties.filePathAsUrl
import com.sk.ai.content.ContentProcessor
import com.sk.ai.util.applyTransformation
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.ai.chat.model.ChatModel
import org.springframework.ai.document.Document
import org.springframework.ai.reader.jsoup.JsoupDocumentReader
import org.springframework.ai.reader.jsoup.config.JsoupDocumentReaderConfig
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component

@Component
@ConditionalOnProperty(prefix = "content.html", name = ["enabled"], havingValue = "true", matchIfMissing = true)
class HtmlProcessor(
        private val contentProperties: ContentProperties,
        private val chatModel: ChatModel,
): ContentProcessor {

    val logger: Logger = LoggerFactory.getLogger(HtmlProcessor::class.java)

    private fun documentsFromFile(filePath: String): List<Document> {
        val config = JsoupDocumentReaderConfig.builder()
                .selector("p") // Extract paragraphs within <article> tags
                .charset("ISO-8859-1")  // Use ISO-8859-1 encoding
                .includeLinkUrls(true) // Include link URLs in metadata
                .metadataTags(listOf("author", "date")) // Extract author and date meta tags
                .additionalMetadata("source", "my-page.html") // Add custom metadata
                .build()
        return JsoupDocumentReader(filePath, config).get()
    }

    override fun get(): List<Document> {
        val htmlFiles = contentProperties.html.filePathAsUrl()
        logger.info("Found following files: $htmlFiles")
        return htmlFiles.flatMap { apply(documentsFromFile(it)) }
    }

    override fun apply(t: List<Document>): List<Document> {
        return contentProperties.html.applyTransformation(chatModel, t)
    }
}