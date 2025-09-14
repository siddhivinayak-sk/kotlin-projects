package com.sk.ai.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "vector", )
data class VectorStoreProperties(val azureAiSearch: AzureAiVectorStoreProperties)

data class AzureAiVectorStoreProperties(
    val enabled: Boolean,
    val endpoint: String,
    val apiKey: String,
    val indexName: String,
    val initializationSchema: Boolean = false,
    val topK: Int = 5,
    val similarityIndexThreshold: Double = 0.7,
    val metadataFields: Map<String, String> = emptyMap(),
)