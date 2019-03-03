package com.junjie.secdraservice.serviceimpl

import com.junjie.secdracore.util.DateUtil
import com.junjie.secdraservice.dao.ISystemMessageDao
import com.junjie.secdraservice.model.SystemMessage
import com.junjie.secdraservice.service.ISystemMessageService
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import java.util.*
import javax.persistence.criteria.Predicate

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

    override fun deleteByMonthAgo() {
        val specification = Specification<SystemMessage> { root, _, criteriaBuilder ->
            val predicatesList = ArrayList<Predicate>()
            predicatesList.add(criteriaBuilder.greaterThan(root.get("createDate"), DateUtil.addDate(Date(), 0, -30, 0, 0, 0, 0, 0)))
            criteriaBuilder.and(*predicatesList.toArray(arrayOfNulls<Predicate>(predicatesList.size)))
        }
        val list = systemMessageDao.findAll(specification)
        for (item in list) {
            systemMessageDao.delete(item)
        }
        return
    }
}