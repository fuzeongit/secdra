package com.junjie.secdraservice.serviceimpl

import com.junjie.secdracore.util.DateUtil
import com.junjie.secdraservice.dao.IReplyMessageDao
import com.junjie.secdraservice.model.ReplyMessage
import com.junjie.secdraservice.service.IReplyMessageService
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import java.util.*
import javax.persistence.criteria.Predicate

@Service
class ReplyMessageService(private val replyMessageDao: IReplyMessageDao) : IReplyMessageService {
    override fun save(replyMessage: ReplyMessage): ReplyMessage {
        return replyMessageDao.save(replyMessage)
    }

    override fun list(criticId: String): List<ReplyMessage> {
        return replyMessageDao.findAllByCriticIdOrderByCreateDateDesc(criticId)
    }

    override fun countUnread(criticId: String): Long {
        return replyMessageDao.countByCriticIdAndIsRead(criticId, false)
    }

    override fun listUnread(criticId: String): List<ReplyMessage> {
        return replyMessageDao.findAllByCriticIdAndIsReadOrderByCreateDateDesc(criticId, false)
    }

    override fun deleteByMonthAgo() {
        val specification = Specification<ReplyMessage> { root, _, criteriaBuilder ->
            val predicatesList = ArrayList<Predicate>()
            predicatesList.add(criteriaBuilder.greaterThan(root.get("createDate"), DateUtil.addDate(Date(), 0, -30, 0, 0, 0, 0, 0)))
            criteriaBuilder.and(*predicatesList.toArray(arrayOfNulls<Predicate>(predicatesList.size)))
        }
        val list = replyMessageDao.findAll(specification)
        for (item in list) {
            replyMessageDao.delete(item)
        }
        return
    }
}