package com.sk.ai.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "app")
data class ModelProperties(
        val models: Map<String, ChatModelConfig> = emptyMap(),
        val embeddings: Map<String, EmbeddingModelConfig> = emptyMap(),
        val chatMemoryCount: Int = 10,
        val chatMemoryType: ChatMemoryType = ChatMemoryType.MESSAGE,
)

data class ChatModelConfig(
        val enabled: Boolean,
        val modelType: ChatModelType,
        val baseUrl: String,
        val modelName: String,
        val apiKey: String,
        val systemPrompt: String,
        val userPrompt: String,
        val maxTokens: Int,
        val temperature: Double,
        val maxCompletionTokens: Int,
        val topP: Double,
        val frequencyPenalty: Double,
        val presencePenalty: Double,
        val n: Int,
)

enum class ChatModelType {
    OAI_GPT_4_1
}

enum class EmbeddingModelType {
    OAI_TEXT_EMBEDDING_3_LARGE
}

enum class ChatMemoryType {
    MESSAGE,
    PROMPT,
}

data class EmbeddingModelConfig(
        val enabled: Boolean,
        val modelType: EmbeddingModelType,
        val baseUrl: String,
        val modelName: String,
        val apiKey: String,
        val user: String?,
        val inputType: String?,
        val dimension: Int?
)
