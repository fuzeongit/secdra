package com.junjie.secdraweb.event

import org.springframework.context.ApplicationListener
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.messaging.simp.user.SimpUserRegistry
import org.springframework.stereotype.Component
import org.springframework.web.socket.messaging.SessionDisconnectEvent

@Component
class WebSocketOnDisconnectEventListener(private val simpUserRegistry: SimpUserRegistry) : ApplicationListener<SessionDisconnectEvent> {
    override fun onApplicationEvent(sessionDisconnectEvent: SessionDisconnectEvent) {
        println("当前在线人数:" + simpUserRegistry.userCount);
        println(sessionDisconnectEvent.user?.name + "已断开")
    }
}