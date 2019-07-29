package com.junjie.secdraadmin.controller

import com.junjie.secdraservice.dao.SystemMessageDAO
import com.junjie.secdraservice.dao.UserDAO
import com.junjie.secdraservice.model.SystemMessage
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("message")
class MessageController(private var userDAO: UserDAO, private var systemMessageDAO: SystemMessageDAO) {
    @PostMapping("/init")
    fun send(title: String?, content: String?): Any {
        val list = userDAO.findAll()
        for (item in list) {
            val systemMessage = SystemMessage()
            systemMessage.title = title
            systemMessage.content = content
            systemMessage.userId = item.id
            systemMessageDAO.save(systemMessage)
        }
        return true
    }
}