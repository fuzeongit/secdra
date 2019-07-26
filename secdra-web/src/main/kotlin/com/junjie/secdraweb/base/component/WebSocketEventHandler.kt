package com.junjie.secdraweb.base.component

import org.springframework.stereotype.Component
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.WebSocketHandler
import org.springframework.web.socket.WebSocketMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping


//@Component
//class WebSocketEventHandler() : WebSocketHandler {
//    override fun handleTransportError(p0: WebSocketSession, p1: Throwable) {
//        println(p0.id)
//    }
//
//    override fun afterConnectionClosed(p0: WebSocketSession, p1: CloseStatus) {
//        println(p0.id)
//    }
//
//    override fun handleMessage(p0: WebSocketSession, p1: WebSocketMessage<*>) {
//        println(p1.payload)
//    }
//
//    override fun afterConnectionEstablished(p0: WebSocketSession) {
//        println(p0.id + "：链接成功")
//    }
//
//    override fun supportsPartialMessages(): Boolean {
//        return true
//    }
////
////    companion object {
////        //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
////        private var webSocketSet = CopyOnWriteArraySet<WebSocketEventHandler>()
////
////        //与某个客户端的连接会话，需要通过它来给客户端发送数据
////        private var session: Session? = null
////    }
////
////    /**
////     * 连接建立成功调用的方法 */
////    @OnOpen
////    fun onOpen(s: Session) {
////        session = s
////        webSocketSet.add(this)     //加入set中
////
////    }
////
////    /**
////     * 连接关闭调用的方法
////     */
////    @OnClose
////    fun onClose() {
////        webSocketSet.remove(this)  //从set中删除
////    }
////
////    /**
////     * 收到客户端消息后调用的方法
////     *
////     * @param message 客户端发送过来的消息
////     */
////    @OnMessage
////    fun onMessage(message: String, session: Session) {
////        println("来自客户端的消息:$message")
////
////        //群发消息
////        for (item in webSocketSet) {
////            try {
////                item.sendMessage(message)
////            } catch (e: IOException) {
////                e.printStackTrace()
////            }
////
////        }
////    }
////
////    @Throws(IOException::class)
////    fun sendMessage(message: String) {
////        session?.basicRemote?.sendText(message)
////    }
//}

@Controller
class WebSocketEventHandler {
    @MessageMapping("/welcome")//1
    @SendTo("/welcome")
    @Throws(Exception::class)
    fun say(message: String): Boolean {
        return true
    }
}