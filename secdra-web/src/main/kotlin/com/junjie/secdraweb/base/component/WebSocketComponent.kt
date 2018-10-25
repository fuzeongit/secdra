package com.junjie.secdraweb.base.component

import org.springframework.stereotype.Component
import java.io.IOException
import java.util.concurrent.CopyOnWriteArraySet
import javax.websocket.*
import javax.websocket.server.ServerEndpoint

@ServerEndpoint(value = "/webSocket")
@Component
class WebSocketComponent {
    companion object {
        //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
        var onlineCount = 0
        //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
        var webSocketSet: CopyOnWriteArraySet<WebSocketComponent> = CopyOnWriteArraySet<WebSocketComponent>()

        /**
         * 群发自定义消息
         * */
        @Throws(IOException::class)
        fun sendInfo(message: String) {
            for (item in webSocketSet) {
                try {
                    item.sendMessage(message)
                } catch (e: IOException) {
                    continue
                }
            }
        }

        @Synchronized
        fun addOnlineCount() {
            WebSocketComponent.onlineCount++
        }

        @Synchronized
        fun subOnlineCount() {
            WebSocketComponent.onlineCount--
        }
    }

    var session: Session? = null

    /**
     * 连接建立成功调用的方法*/
    @OnOpen
    fun onOpen(session: Session) {
        this.session = session
        webSocketSet.add(this)     //加入set中
        addOnlineCount()           //在线数加1
        try {
            sendMessage("连接成功")
        } catch (e: IOException) {

        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    fun onClose() {
        webSocketSet.remove(this)  //从set中删除
        subOnlineCount()           //在线数减1
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     * */
    @OnMessage
    fun onMessage(message: String, session: Session) {
        //群发消息
        for (item in webSocketSet) {
            try {
                item.sendMessage(message)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    @OnError
    fun onError(session: Session, error: Throwable) {
        error.printStackTrace()
    }

    @Throws(IOException::class)
    fun sendMessage(message: String) {
        this.session?.basicRemote?.sendText(message)
    }
}
