package com.junjie.secdraservice.serviceimpl

import com.junjie.secdracore.util.DateUtil
import com.junjie.secdradata.database.primary.dao.SystemMessageDAO
import com.junjie.secdradata.database.primary.entity.SystemMessage
import com.junjie.secdraservice.service.SystemMessageService
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import java.util.*
import javax.persistence.criteria.Predicate

@Service
class SystemMessageServiceImpl(private val systemMessageDAO: SystemMessageDAO) : SystemMessageService {
    override fun save(systemMessage: SystemMessage): SystemMessage {
        return systemMessageDAO.save(systemMessage)
    }

    override fun list(userId: String): List<SystemMessage> {
        return systemMessageDAO.findAllByUserIdOrderByCreateDateDesc(userId)
    }

    override fun countUnread(userId: String): Long {
        return systemMessageDAO.countByUserIdAndReview(userId,false)
    }

    override fun listUnread(userId: String): List<SystemMessage> {
        return systemMessageDAO.findAllByUserIdAndReviewOrderByCreateDateDesc(userId,false)
    }

    override fun deleteByMonthAgo() {
        val specification = Specification<SystemMessage> { root, _, criteriaBuilder ->
            val predicatesList = ArrayList<Predicate>()
            predicatesList.add(criteriaBuilder.greaterThan(root.get("createDate"), DateUtil.addDate(Date(), 0, -30, 0, 0, 0, 0, 0)))
            criteriaBuilder.and(*predicatesList.toArray(arrayOfNulls<Predicate>(predicatesList.size)))
        }
        val list = systemMessageDAO.findAll(specification)
        for (item in list) {
            systemMessageDAO.delete(item)
        }
        return
    }
}