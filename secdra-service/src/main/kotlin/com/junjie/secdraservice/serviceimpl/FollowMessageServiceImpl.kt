package com.junjie.secdraservice.serviceimpl

import com.junjie.secdracore.util.DateUtil
import com.junjie.secdraservice.dao.FollowMessageDAO
import com.junjie.secdraservice.model.FollowMessage
import com.junjie.secdraservice.service.FollowMessageService
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import java.util.*
import javax.persistence.criteria.Predicate

@Service
class FollowMessageServiceImpl(private val followMessageDAO: FollowMessageDAO) : FollowMessageService {
    override fun save(followMessage: FollowMessage): FollowMessage {
        return followMessageDAO.save(followMessage)
    }

    override fun list(followingId: String): List<FollowMessage> {
        return followMessageDAO.findAllByFollowingIdOrderByCreateDateDesc(followingId)
    }

    override fun countUnread(followingId: String): Long {
        return followMessageDAO.countByFollowingIdAndIsRead(followingId, false)
    }

    override fun listUnread(followingId: String): List<FollowMessage> {
        return followMessageDAO.findAllByFollowingIdAndIsReadOrderByCreateDateDesc(followingId, false)
    }

    override fun deleteByMonthAgo() {
        val specification = Specification<FollowMessage> { root, _, criteriaBuilder ->
            val predicatesList = ArrayList<Predicate>()
            predicatesList.add(criteriaBuilder.lessThan(root.get("createDate"), DateUtil.addDate(Date(), 0, -30, 0, 0, 0, 0, 0)))
            criteriaBuilder.and(*predicatesList.toArray(arrayOfNulls<Predicate>(predicatesList.size)))
        }
        val list = followMessageDAO.findAll(specification)
        for (item in list) {
            followMessageDAO.delete(item)
        }
        return
    }

}