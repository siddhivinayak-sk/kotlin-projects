package com.sk.ai.util

import com.azure.ai.openai.OpenAIClientBuilder
import com.azure.core.credential.AzureKeyCredential
import com.sk.ai.config.properties.ChatModelProperties
import com.sk.ai.config.properties.ChatModelType.OAI_GPT_4_1
import com.sk.ai.config.properties.ChatModelType.OLLAMA_LLM
import com.sk.ai.config.properties.ImageModelProperties
import com.sk.ai.config.properties.ImageModelType.OAI_DALLE_3
import org.springframework.ai.azure.openai.AzureOpenAiChatModel
import org.springframework.ai.azure.openai.AzureOpenAiChatOptions
import org.springframework.ai.azure.openai.AzureOpenAiImageModel
import org.springframework.ai.azure.openai.AzureOpenAiImageOptions
import org.springframework.ai.chat.model.ChatModel
import org.springframework.ai.chat.prompt.ChatOptions
import org.springframework.ai.image.ImageModel
import org.springframework.ai.image.ImageOptions
import org.springframework.ai.ollama.OllamaChatModel
import org.springframework.ai.ollama.api.OllamaApi
import org.springframework.ai.ollama.api.OllamaOptions
import org.springframework.http.client.SimpleClientHttpRequestFactory
import org.springframework.web.client.RestClient
import org.springframework.web.reactive.function.client.WebClient

fun ChatModelProperties.chatModelOptions(): ChatOptions {
    return when (modelType) {
        OAI_GPT_4_1 -> azureChatModelOptions()
        OLLAMA_LLM -> ollamaChatModelOptions()
    }
}

fun ChatModelProperties.toChatModel(chatOptions: ChatOptions): ChatModel {
    return when (modelType) {
        OAI_GPT_4_1 -> createAzureOpenAiModel(chatOptions)
        OLLAMA_LLM -> createOllamaModel(chatOptions)
    }
}

fun ChatModelProperties.azureChatModelOptions(): ChatOptions {
    return AzureOpenAiChatOptions().also {
        it.user = userPrompt
        it.topP = topP
        it.temperature = temperature
        it.maxTokens = maxTokens
        it.frequencyPenalty = frequencyPenalty
        it.presencePenalty = presencePenalty
        it.n = n
        it.deploymentName = modelName
    }
}

fun ChatModelProperties.ollamaChatModelOptions(): ChatOptions {
    return OllamaOptions.builder()
            .topP(topP)
            .temperature(temperature)
            .presencePenalty(presencePenalty)
            .frequencyPenalty(frequencyPenalty)
            .model(modelName)
            .topK(n)
            .numCtx(maxTokens)
            .build()
}

fun ChatModelProperties.createAzureOpenAiModel(chatOptions: ChatOptions): ChatModel {
    val oaiClientBuilder = OpenAIClientBuilder()
            .credential(AzureKeyCredential(apiKey))
            .endpoint(baseUrl)
    return AzureOpenAiChatModel.builder()
            .defaultOptions(chatOptions as AzureOpenAiChatOptions)
            .openAIClientBuilder(oaiClientBuilder)
            .build()
}

fun ChatModelProperties.createOllamaModel(chatOptions: ChatOptions): ChatModel {
    val restClient = RestClient.builder()
            .requestFactory(
                    SimpleClientHttpRequestFactory()
                            .also {
                                it.setReadTimeout(0)
                                it.setConnectTimeout(0)
                            })
    val webClient = WebClient.builder()
    val ollamaApi = OllamaApi.builder()
            .baseUrl(baseUrl)
            .webClientBuilder(webClient)
            .restClientBuilder(restClient)
            .build()
    return OllamaChatModel.builder()
            .ollamaApi(ollamaApi)
            .defaultOptions(chatOptions as OllamaOptions)
            .build()
}

fun ImageModelProperties.toImageModelOptions(): ImageOptions {
    return when(modelType) {
        OAI_DALLE_3 -> toAzureOpenAIDalleOptions()
    }
}

fun ImageModelProperties.toImageModel(): ImageModel {
    return when(modelType) {
        OAI_DALLE_3 -> toOpenAIDalleModel()
    }
}

fun ImageModelProperties.toAzureOpenAIDalleOptions(): ImageOptions {
    return AzureOpenAiImageOptions.builder()
            .N(n)
            .responseFormat(responseFormat)
            .user(user)
            .style(style)
            .height(height)
            .width(width)
            .model(modelName)
            .build()
}

fun ImageModelProperties.toOpenAIDalleModel(): ImageModel {
    val oaiClientBuilder = OpenAIClientBuilder()
        .credential(AzureKeyCredential(apiKey))
        .endpoint(baseUrl)
        .buildClient()
    return AzureOpenAiImageModel(oaiClientBuilder, toImageModelOptions() as AzureOpenAiImageOptions)
}
