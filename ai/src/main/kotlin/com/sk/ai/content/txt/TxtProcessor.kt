package com.sk.ai.content.txt

import com.sk.ai.config.properties.ContentProperties
import com.sk.ai.config.properties.filePathAsUrl
import com.sk.ai.content.ContentProcessor
import com.sk.ai.util.applyTransformation
import org.springframework.ai.chat.model.ChatModel
import org.springframework.ai.document.Document
import org.springframework.ai.reader.TextReader
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component

@Component
@ConditionalOnProperty(prefix = "content.txt", name = ["enabled"], havingValue = "true", matchIfMissing = true)
class TxtProcessor(
        private val contentProperties: ContentProperties,
        private val chatModel: ChatModel,
): ContentProcessor {

    private fun documentsFromFile(file: String): List<Document> {
        val textReader = TextReader(file)
        textReader.customMetadata["filename"] = "text-source.txt"
        return textReader.read()
    }

    override fun get(): List<Document> {
        return contentProperties.txt.filePathAsUrl().flatMap { apply(documentsFromFile(it)) }
    }

    override fun apply(t: List<Document>): List<Document> {
        return contentProperties.html.applyTransformation(chatModel, t)
    }
}