package com.junjie.secdraadmin.controller

import com.junjie.secdraservice.dao.ISystemMessageDao
import com.junjie.secdraservice.dao.IUserDao
import com.junjie.secdraservice.model.SystemMessage
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("message")
class MessageController(private var userDao: IUserDao, private var systemMessageDao: ISystemMessageDao) {
    @PostMapping("/init")
    fun send(title: String?, content: String?): Any {
        val list = userDao.findAll()
        for (item in list) {
            val systemMessage = SystemMessage()
            systemMessage.title = title
            systemMessage.content = content
            systemMessage.userId = item.id
            systemMessageDao.save(systemMessage)
        }
        return true
    }
}