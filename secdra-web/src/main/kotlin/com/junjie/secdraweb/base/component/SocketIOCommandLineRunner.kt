package com.junjie.secdraweb.base.component

import com.corundumstudio.socketio.SocketIOServer
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

/**
 * @author fjj
 * socketIo 服务
 */
@Component
class SocketIOCommandLineRunner(private val socketIOServer: SocketIOServer) : CommandLineRunner {
    @Throws(Exception::class)
    override fun run(vararg args: String?) {
//        socketIOServer.start();
//        System.out.println("socket.io启动成功！")
    }
}
