package com.sk.ai.content.tika

import com.sk.ai.config.properties.ContentProperties
import com.sk.ai.config.properties.filePathAsUrl
import com.sk.ai.content.ContentProcessor
import com.sk.ai.util.applyTransformation
import org.springframework.ai.chat.model.ChatModel
import org.springframework.ai.document.Document
import org.springframework.ai.reader.tika.TikaDocumentReader
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component

@Component
@ConditionalOnProperty(prefix = "content.tika", name = ["enabled"], havingValue = "true", matchIfMissing = true)
class TikaProcessor(
        private val contentProperties: ContentProperties,
        private val chatModel: ChatModel,
): ContentProcessor {

    private fun documentsFromFile(filePath: String) : List<Document> {
        return TikaDocumentReader(filePath).read()
    }

    override fun get(): List<Document> {
        return contentProperties.tika.filePathAsUrl().flatMap { apply(documentsFromFile(it)) }
    }

    override fun apply(t: List<Document>): List<Document> {
        return contentProperties.tika.applyTransformation(chatModel, t)
    }
}
