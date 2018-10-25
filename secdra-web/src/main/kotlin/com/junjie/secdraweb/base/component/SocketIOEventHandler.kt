package com.junjie.secdraweb.base.component

import com.corundumstudio.socketio.AckRequest
import com.corundumstudio.socketio.SocketIOClient
import com.corundumstudio.socketio.SocketIOServer
import com.corundumstudio.socketio.annotation.OnConnect
import com.corundumstudio.socketio.annotation.OnDisconnect
import com.corundumstudio.socketio.annotation.OnEvent
import org.springframework.stereotype.Component
import java.util.*
import kotlin.collections.ArrayList

@Component
class SocketIOEventHandler(private var socketIoServer: SocketIOServer) {
    companion object {
        var listClient = ArrayList<UUID>()
        var sessionId:UUID? = null
        var limitSeconds = 60
    }

    @OnConnect
    fun onConnect(client: SocketIOClient) {
        sessionId = client.sessionId
        listClient.add(client.sessionId);
        System.out.println("客户端:" + client.sessionId + "已连接");
    }

    @OnDisconnect
    fun onDisconnect(client: SocketIOClient) {
        System.out.println("客户端:" + client.sessionId + "断开连接");
    }


    @OnEvent(value = "messageEvent")
    fun onEvent(client: SocketIOClient, request: AckRequest, message: String) {
        System.out.println("发来消息：$message");
        socketIoServer.getClient(client.sessionId).sendEvent("messageEvent", "back data");
    }

    fun sendBuyLogEvent() {   //这里就是向客户端推消息了
        val dateTime = Date().toString();
        for (clientId in listClient) {
            if (socketIoServer.getClient(clientId) == null) continue;
            socketIoServer.getClient(clientId).sendEvent("send", dateTime, 1);
        }
    }
}
