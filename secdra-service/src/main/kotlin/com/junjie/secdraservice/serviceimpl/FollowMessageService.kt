package com.junjie.secdraservice.serviceimpl

import com.junjie.secdracore.util.DateUtil
import com.junjie.secdraservice.dao.IFollowMessageDao
import com.junjie.secdraservice.model.FollowMessage
import com.junjie.secdraservice.service.IFollowMessageService
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import java.util.*
import javax.persistence.criteria.Predicate

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

    override fun deleteByMonthAgo() {
        val specification = Specification<FollowMessage> { root, _, criteriaBuilder ->
            val predicatesList = ArrayList<Predicate>()
            predicatesList.add(criteriaBuilder.greaterThan(root.get("createDate"), DateUtil.addDate(Date(), 0, -30, 0, 0, 0, 0, 0)))
            criteriaBuilder.and(*predicatesList.toArray(arrayOfNulls<Predicate>(predicatesList.size)))
        }
        val list = followMessageDao.findAll(specification)
        for (item in list) {
            followMessageDao.delete(item)
        }
        return
    }

}