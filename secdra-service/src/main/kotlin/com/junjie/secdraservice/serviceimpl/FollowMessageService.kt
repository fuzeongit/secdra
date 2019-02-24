package com.junjie.secdraservice.serviceimpl

import com.junjie.secdraservice.dao.IFollowMessageDao
import com.junjie.secdraservice.model.FollowMessage
import com.junjie.secdraservice.service.IFollowMessageService
import org.springframework.stereotype.Service

@Service
class FollowMessageService(private val followMessageDao: IFollowMessageDao) : IFollowMessageService {
    override fun save(followMessage: FollowMessage): FollowMessage {
        return followMessageDao.save(followMessage)
    }

    override fun list(followingId: String): List<FollowMessage> {
        return followMessageDao.findAllByFollowingIdOrderByCreateDateDesc(followingId)
    }

    override fun countUnread(followingId: String): Long {
        return followMessageDao.countByFollowingIdAndIsRead(followingId, false)
    }

    override fun listUnread(followingId: String): List<FollowMessage> {
        return followMessageDao.findAllByFollowingIdAndIsReadOrderByCreateDateDesc(followingId, false)
    }
}