package com.sk.ai.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "app")
data class ModelProperties(
        val models: Map<String, ChatModelProperties> = emptyMap(),
        val embeddings: Map<String, EmbeddingModelConfig> = emptyMap(),
        val images: Map<String, ImageModelProperties> = emptyMap(),
        val chatMemoryCount: Int = 10,
        val chatMemoryType: ChatMemoryType = ChatMemoryType.MESSAGE,
)

data class ChatModelProperties(
        val enabled: Boolean,
        val modelType: ChatModelType,
        val baseUrl: String,
        val modelName: String,
        val apiKey: String = "",
        val systemPrompt: String,
        val userPrompt: String,
        val maxTokens: Int,
        val temperature: Double,
        val maxCompletionTokens: Int = 0,
        val topP: Double,
        val frequencyPenalty: Double,
        val presencePenalty: Double,
        val n: Int,
)

data class ImageModelProperties(
        val enabled: Boolean,
        val modelType: ImageModelType,
        val baseUrl: String = "",
        val apiKey: String = "",
        val modelName: String = "",
        val n: Int = 1,
        val quality: String = "standard",
        val width: Int = 1024,
        val height: Int = 1024,
        val responseFormat: String = "b64_json",
        val user: String = "",
        val style: String = "natural",
)

enum class ImageModelType {
    OAI_DALLE_3,
}

enum class ChatModelType {
    OAI_GPT_4_1,
    OLLAMA_LLM,
}

enum class EmbeddingModelType {
    OAI_TEXT_EMBEDDING_3_LARGE,
    POSTGRES_ML_DISTILBERT_BASE_UNCASED,
}

enum class ChatMemoryType {
    MESSAGE,
    PROMPT,
    VECTOR,
}

data class EmbeddingModelConfig(
        val enabled: Boolean,
        val modelType: EmbeddingModelType,
        val baseUrl: String = "",
        val modelName: String = "",
        val apiKey: String = "",
        val user: String? = null,
        val inputType: String? = null,
        val dimension: Int? = null,
        val kwargs: Map<String, Any> = emptyMap(),
        val transformer: String = "",
)
