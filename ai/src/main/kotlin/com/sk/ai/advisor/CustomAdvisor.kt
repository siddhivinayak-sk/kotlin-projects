package com.sk.ai.advisor

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.ai.chat.client.ChatClientRequest
import org.springframework.ai.chat.client.ChatClientResponse
import org.springframework.ai.chat.client.advisor.api.CallAdvisor
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain
import org.springframework.ai.chat.client.advisor.api.StreamAdvisor
import org.springframework.ai.chat.client.advisor.api.StreamAdvisorChain
import reactor.core.publisher.Flux

class CustomAdvisor: CallAdvisor, StreamAdvisor {

    val logger: Logger = LoggerFactory.getLogger(CustomAdvisor::class.java)

    override fun adviseCall(
            chatClientRequest: ChatClientRequest,
            callAdvisorChain: CallAdvisorChain,
    ): ChatClientResponse {
        logger.info("Custom Advisor call started")
        val retVal =  callAdvisorChain.nextCall(chatClientRequest)
        logger.info("Custom Advisor call completed")
        return  retVal
    }

    override fun getName() = "CustomAdvisor"

    override fun getOrder() = 0

    override fun adviseStream(
            chatClientRequest: ChatClientRequest,
            streamAdvisorChain: StreamAdvisorChain,
    ): Flux<ChatClientResponse?> {
        logger.info("Custom Advisor stream started")
        return streamAdvisorChain.nextStream(chatClientRequest)
                .doOnComplete { logger.info("Custom Advisor stream completed") }
    }
}