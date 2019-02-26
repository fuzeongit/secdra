package com.junjie.secdraservice.serviceimpl

import com.junjie.secdraservice.dao.ISystemMessageDao
import com.junjie.secdraservice.model.SystemMessage
import com.junjie.secdraservice.service.ISystemMessageService
import org.springframework.stereotype.Service

@Service
class SystemMessageService(private val systemMessageDao: ISystemMessageDao) : ISystemMessageService {
    override fun save(systemMessage: SystemMessage): SystemMessage {
        return systemMessageDao.save(systemMessage)
    }

    override fun list(userId: String): List<SystemMessage> {
        return systemMessageDao.findAllByUserIdOrderByCreateDateDesc(userId)
    }

    override fun countUnread(userId: String): Long {
        return systemMessageDao.countByUserIdAndIsRead(userId,false)
    }

    override fun listUnread(userId: String): List<SystemMessage> {
        return systemMessageDao.findAllByUserIdAndIsReadOrderByCreateDateDesc(userId,false)
    }
}