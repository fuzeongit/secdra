package com.junjie.secdraservice.service

import com.junjie.secdraservice.dao.INotifyDao
import com.junjie.secdraservice.dao.IReplyDao
import com.junjie.secdraservice.model.Notify
import com.junjie.secdraservice.model.Reply
import org.springframework.stereotype.Service

@Service
class NotifyService(private val notifyDao: INotifyDao) : INotifyService {
    override fun save(notify: Notify): Notify {
        return notifyDao.save(notify)
    }

    override fun list(receiveId: String): List<Notify> {
        return notifyDao.findAllByReceiveIdOrderByCreateDateDesc(receiveId)
    }

    override fun countUnread(receiveId: String): Long {
        return notifyDao.countByReceiveIdAndIsRead(receiveId, false)
    }

    override fun listUnread(receiveId: String): List<Notify> {
        return notifyDao.findAllByReceiveIdAndIsReadOrderByCreateDateDesc(receiveId, false)
    }
}