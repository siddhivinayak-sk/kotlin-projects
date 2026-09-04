package com.sk.project11.controllers

import org.springframework.ai.chat.messages.UserMessage
import org.springframework.ai.chat.model.ChatModel
import org.springframework.ai.chat.model.ChatResponse
import org.springframework.ai.chat.prompt.Prompt
import org.springframework.ai.embedding.EmbeddingModel
import org.springframework.ai.embedding.EmbeddingResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import java.util.List

@RestController
class ChatController(
    val ollamaChatClient: ChatModel,
    val embeddingClient: EmbeddingModel,
) {

    @GetMapping("/ai/generate")
    fun generate(@RequestParam(value = "message", defaultValue = "Tell me a joke") message: String): Map<*, *>? {
        val prompt = Prompt(UserMessage(message))
        val chatResponse = ollamaChatClient.call(prompt)
        return mapOf(
            "content" to chatResponse.result.output.text,
            "messageType" to chatResponse.result.output.messageType,
            "properties" to chatResponse.result.output.metadata,
        )
    }

    @GetMapping("/ai/generateStream")
    fun generateStream(@RequestParam(value = "message", defaultValue = "Tell me a joke") message: String): Flux<ChatResponse?>? {
        val prompt = Prompt(UserMessage(message))
        return ollamaChatClient.stream(prompt)
    }

    @GetMapping("/ai/embedding")
    fun embed(@RequestParam(value = "message", defaultValue = "Tell me a joke") message: String): Map<*, *>? {
        val embeddingResponse: EmbeddingResponse = embeddingClient.embedForResponse(List.of(message))
        return java.util.Map.of<String, Any>("embedding", embeddingResponse)
    }
}
