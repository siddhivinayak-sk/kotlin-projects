package com.sk.ai.controller

import com.sk.ai.content.ContentProcessor
import org.springframework.ai.document.Document
import org.springframework.ai.embedding.EmbeddingModel
import org.springframework.ai.vectorstore.SearchRequest
import org.springframework.ai.vectorstore.VectorStore
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RequestMapping("/embeddings")
@RestController
@ConditionalOnProperty(prefix = "app.chat", name = ["enabled"], havingValue = "true", matchIfMissing = false)
class EmbeddingController(
        private val embeddingModel: EmbeddingModel,
        private val contentProcessors: List<ContentProcessor>,
        private val vectorStore: VectorStore?,
) {

    @GetMapping("/embeddingResponse")
    fun getEmbeddingResponse(@RequestParam text: String): Mono<FloatArray> {
        return Mono.just(embeddingModel.embed(text))
    }

    @GetMapping("/vectors")
    fun getVectors(): String {
        val defaultDocuments = listOf(
                Document("Spring AI rocks!! Spring AI rocks!! Spring AI rocks!! Spring AI rocks!! Spring AI rocks!!", mapOf("country" to "BG", "year" to 2020)),
                Document("The World is Big and Salvation Lurks Around the Corner"),
                Document("You walk forward facing the past and you turn back toward the future.", mapOf("country" to "NL", "year" to 2023))
        )
        val processorDocs = contentProcessors.flatMap { it.get() }
        val docs = takeIf { processorDocs.isNotEmpty() }?.let{ processorDocs } ?: defaultDocuments
        vectorStore?.add(docs)
        return "ok"
    }

    @GetMapping("/vectorStoreResponse")
    fun getVectorStoreResponse(@RequestParam query: String): List<Document>? {
        return vectorStore?.similaritySearch(query)
    }

    @GetMapping("/vectorStoreResponse2")
    fun getVectorStoreResponse2(@RequestParam query: String): List<Document>? {
        val searchRequest = SearchRequest.builder()
                .query(query)
                .build()
        return vectorStore?.similaritySearch(searchRequest)
    }

    @GetMapping("/vectorStoreResponse3")
    fun getVectorStoreResponse3(@RequestParam query: String): List<Document>? {
        val searchRequest = SearchRequest.builder()
                .query(query)
                .filterExpression("country in ['UK', 'NL'] && year >= 2020")
                .build()
        return vectorStore?.similaritySearch(searchRequest)
    }

    @GetMapping("/vectorStoreResponse4")
    fun getVectorStoreResponse4(@RequestParam query: String): List<Document>? {
        val filterExpression = FilterExpressionBuilder().let {
            it.and(it.`in`("country", "UK", "NL"), it.gte("year", 2020)).build()
        }
        val searchRequest = SearchRequest.builder()
                .query(query)
                .filterExpression(filterExpression)
                .build()
        return vectorStore?.similaritySearch(searchRequest)
    }
}