package com.sk.ai.content.custom.transformer

import org.springframework.ai.document.Document
import org.springframework.ai.document.DocumentTransformer

/**
 * DataSanitizer is a custom transformer that can be used to sanitize sensitive data from documents.
 * Implement the apply method to define the sanitization logic.
 */
class DataSanitizer: DocumentTransformer {

    override fun apply(input: List<Document>): List<Document> {
        return input.map {
            Document.builder()
                    .id(it.id)
                    .text(it.text
                                  ?.replace("\u0000", "", ignoreCase = true)
                                  ?.replace("\\x00", "")
                                  ?.replace("\\0", "")
                    )
                    .metadata(it.metadata)
                    .media(it.media)
                    .score(it.score)
                    .build()
        }.filter { !it.text.isNullOrEmpty() }
    }
}