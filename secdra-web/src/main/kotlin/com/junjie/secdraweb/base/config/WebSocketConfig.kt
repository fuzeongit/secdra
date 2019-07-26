package com.junjie.secdraweb.base.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.messaging.simp.config.MessageBrokerRegistry




@Configuration
//class WebSocketConfig : WebSocketConfigurer {
//    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
//        // added end point,
//        // eg. ws://localhost:8080/ws
//
//        // added WebSocketEventHandler to /ws
//        registry.addHandler(webSocketEventHandler(), "/ws").setAllowedOrigins("*")
//
//    }
//
//    @Bean
//    fun webSocketEventHandler(): WebSocketEventHandler {
//        return WebSocketEventHandler()
//    }
//}
class WebSocketConfig : WebSocketMessageBrokerConfigurer {

    override fun registerStompEndpoints(registry: StompEndpointRegistry) {
        registry.addEndpoint("/ws").withSockJS()
    }

}