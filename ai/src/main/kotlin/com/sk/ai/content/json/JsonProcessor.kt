package com.sk.ai.content.json

import com.sk.ai.config.properties.ContentProperties
import com.sk.ai.config.properties.filePathAsUrl
import com.sk.ai.content.ContentProcessor
import com.sk.ai.util.applyTransformation
import org.springframework.ai.chat.model.ChatModel
import org.springframework.ai.document.Document
import org.springframework.ai.reader.JsonReader
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.core.io.UrlResource
import org.springframework.stereotype.Component

@Component
@ConditionalOnProperty(prefix = "content.json", name = ["enabled"], havingValue = "true", matchIfMissing = true)
class JsonProcessor(
        private val contentProperties: ContentProperties,
        private val chatModel: ChatModel,
): ContentProcessor {

    private fun documentsFromFile(file: String): List<Document> {
        return JsonReader(UrlResource(file), contentProperties.json.txtField).get()
    }

    override fun get(): List<Document> {
        return contentProperties.json.filePathAsUrl().flatMap { apply(documentsFromFile(it)) }
    }

    override fun apply(t: List<Document>): List<Document> {
        return contentProperties.json.applyTransformation(chatModel, t)
    }
}