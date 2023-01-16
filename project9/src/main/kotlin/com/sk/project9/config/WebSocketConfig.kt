package com.sk.project9.config

import org.springframework.context.annotation.Configuration
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer

@Configuration
@EnableWebSocketMessageBroker
class WebSocketConfig: WebSocketMessageBrokerConfigurer {

    override fun configureMessageBroker(registry: MessageBrokerRegistry) {
        super.configureMessageBroker(registry)

        // Simple broker
        //registry.enableSimpleBroker("/topic")

        // Relay broker
        registry.enableStompBrokerRelay("/topic")
            .setRelayHost("127.0.0.0")
            .setRelayPort(5673)
            .setClientLogin("admin")
            .setClientPasscode("admin")

        registry.setApplicationDestinationPrefixes("/app")
    }

    override fun registerStompEndpoints(registry: StompEndpointRegistry) {
        super.registerStompEndpoints(registry)
        registry.addEndpoint("/gs-guide-websocket").withSockJS()
    }

}