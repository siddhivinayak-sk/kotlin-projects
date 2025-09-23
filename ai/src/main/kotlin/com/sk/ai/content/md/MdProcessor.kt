package com.sk.ai.content.md

import com.sk.ai.config.properties.ContentProperties
import com.sk.ai.config.properties.filePathAsUrl
import com.sk.ai.content.ContentProcessor
import com.sk.ai.util.applyTransformation
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.ai.chat.model.ChatModel
import org.springframework.ai.document.Document
import org.springframework.ai.reader.markdown.MarkdownDocumentReader
import org.springframework.ai.reader.markdown.config.MarkdownDocumentReaderConfig
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component

@Component
@ConditionalOnProperty(prefix = "content.md", name = ["enabled"], havingValue = "true", matchIfMissing = true)
class MdProcessor(
        private val contentProperties: ContentProperties,
        private val chatModel: ChatModel,
): ContentProcessor {

    val logger: Logger = LoggerFactory.getLogger(MdProcessor::class.java)

    private fun documentsFromFile(filePath: String): List<Document> {
        val config = MarkdownDocumentReaderConfig.builder()
                .withHorizontalRuleCreateDocument(true)
                .withIncludeCodeBlock(false)
                .withIncludeBlockquote(false)
                .withAdditionalMetadata("filename", "code.md")
                .build()
        return MarkdownDocumentReader(filePath, config).get()
    }

    override fun get(): List<Document> {
        val mdFiles = contentProperties.md.filePathAsUrl()
        logger.info("Found following files: $mdFiles")
        return mdFiles.flatMap { apply(documentsFromFile(it)) }
    }

    override fun apply(t: List<Document>): List<Document> {
        return contentProperties.md.applyTransformation(chatModel, t)
    }
}