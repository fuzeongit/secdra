package com.junjie.secdraweb.base.component

import com.corundumstudio.socketio.SocketIOServer
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

@Component
class SocketIOCommandLineRunner(private val server: SocketIOServer) : CommandLineRunner {
    @Throws(Exception::class)
    override fun run(vararg args: String?) {
        server.start();
        System.out.println("socket.io启动成功！")
    }
}