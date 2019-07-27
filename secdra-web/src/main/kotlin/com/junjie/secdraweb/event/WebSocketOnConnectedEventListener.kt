package com.junjie.secdraweb.event

import org.springframework.web.socket.messaging.SessionConnectedEvent
import org.springframework.context.ApplicationListener
import org.springframework.messaging.simp.user.SimpUserRegistry
import org.springframework.stereotype.Component


@Component
class WebSocketOnConnectedEventListener(private val simpUserRegistry: SimpUserRegistry) : ApplicationListener<SessionConnectedEvent> {
    override fun onApplicationEvent(sessionConnectEvent: SessionConnectedEvent) {
        println("当前在线人数:" + simpUserRegistry.userCount);
        println(sessionConnectEvent.user!!.name + "已连接")
    }
}