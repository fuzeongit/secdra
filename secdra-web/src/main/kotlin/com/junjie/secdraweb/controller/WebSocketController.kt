package com.junjie.secdraweb.controller

import com.junjie.secdracore.model.Result
import com.junjie.secdraweb.base.component.BaseConfig
import org.springframework.messaging.Message
import org.springframework.messaging.handler.annotation.Headers
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.messaging.simp.annotation.SendToUser
import org.springframework.messaging.simp.user.SimpUserRegistry
import org.springframework.stereotype.Controller
import java.security.Principal

@Controller
class WebSocketController(private val baseConfig: BaseConfig, private val simpMessagingTemplate: SimpMessagingTemplate, private val simpUserRegistry: SimpUserRegistry) {
    @MessageMapping("/comment")
    fun send(message: Any, @Headers headers: Map<String, Any>, principal: Principal) {
        val receiverList = simpUserRegistry.users.filter { it ->
            println(it.name)
            println("402880e566ddba740166ddbce0b70000")
            println(it.name == "402880e566ddba740166ddbce0b70000")
            it.name == "402880e566ddba740166ddbce0b70000"
        }
        for (receiver in receiverList) {
            simpMessagingTemplate.convertAndSendToUser(receiver.name, "/commentNotice", "你还没有登录哦");
        }
    }

    @MessageMapping("/sendBroadcast")
    @SendTo("/broadcast")
    fun sendBroadcast(message: Message<Map<String, Any>>, a: String?) {
        sendBroadcast()
    }

    @MessageMapping("/sendBroadcastBySendToUser")
    @SendToUser("sendBroadcastBySendToUser")
    fun sendBroadcastBySendToUser(message: Any, @Headers headers: Map<String, Any>): String {
        return "就发给你自己"
    }

    @MessageMapping("/sendBroadcastBySendTo")
    @SendTo("sendBroadcastBySendTo")
    fun sendBroadcastBySendTo(message: Any, @Headers headers: Map<String, Any>): String {
        return "send to all"
    }

    private fun sendBroadcast() {
        simpMessagingTemplate.convertAndSend("/broadcast", Result(200, null, null))
    }
}