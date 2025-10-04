package com.sk.ai.util

import com.sk.ai.config.properties.ContentDetail
import com.sk.ai.config.properties.TransformerConfig
import com.sk.ai.content.custom.transformer.DataSanitizer
import org.springframework.ai.chat.model.ChatModel
import org.springframework.ai.document.Document
import org.springframework.ai.model.transformer.KeywordMetadataEnricher
import org.springframework.ai.model.transformer.SummaryMetadataEnricher
import org.springframework.ai.transformer.splitter.TokenTextSplitter
import java.security.InvalidParameterException

fun ContentDetail.applyTransformation(chatModel: ChatModel, documents: List<Document>): List<Document> {
    // Chain all transformers in order, passing the result of one as input to the next
    return transformers.entries.filter{ it.value.enabled }.fold(documents) { acc, entry ->
        entry.constructAndApplyTransformation(chatModel, acc)
    }
}

private fun Map.Entry<String, TransformerConfig>.constructAndApplyTransformation(chatModel: ChatModel, documents: List<Document>): List<Document> {
    return when(key) {
        "sanitizer" -> DataSanitizer().apply(documents)
        "splitter" -> TokenTextSplitter(value.defaultChunkSize, value.minChunkSizeChars, value.minChunkLengthToEmbed, value.maxNumChunks, value.keepSeparator).apply(documents)
        "keyword" -> KeywordMetadataEnricher(chatModel, value.keywordCount).apply(documents)
        "summary" -> SummaryMetadataEnricher(chatModel, value.summaryTypes).apply(documents)
        else -> throw InvalidParameterException("unsupported transformer type: $key")
    }
}