package com.sk.ai.controller

import io.modelcontextprotocol.client.McpAsyncClient
import io.modelcontextprotocol.client.McpSyncClient
import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor
import org.springframework.ai.chat.memory.MessageWindowChatMemory
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/mcp-client")
@RestController
class McpClientController(
        private val mcpSyncClients: List<McpSyncClient>,
        private val mcpAsyncClients: List<McpAsyncClient>,
        private val chatClient: ChatClient?,
) {

    @GetMapping("/sync")
    fun sync(@RequestParam("query") query: String): String {
        return createSyncChat(query)
    }

    private fun createSyncChat(query: String): String {
        return chatClient
                ?.prompt()
                ?.system("You are useful assistant and can perform web searches using domain specific registered MCP servers reply to your questions.")
                ?.user(query)
                ?.toolCallbacks(SyncMcpToolCallbackProvider(mcpSyncClients))
                ?.advisors(MessageChatMemoryAdvisor.builder(MessageWindowChatMemory.builder().build()).build())
                ?.call()
                ?.content()
                ?: "No chat client configured"
    }
}