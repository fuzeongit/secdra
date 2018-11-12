package com.junjie.secdraservice.service

import com.junjie.secdraservice.dao.IFocusDrawDao
import com.junjie.secdraservice.model.FocusDraw
import org.springframework.stereotype.Service

@Service
class FocusDrawService(private val focusDrawDao: IFocusDrawDao) : IFocusDrawService {
    override fun exists(userId: String, drawId: String): Boolean {
        return focusDrawDao.existsByUserIdAndDrawId(userId, drawId)
    }


    override fun get(userId: String, drawId: String): FocusDraw {
        return focusDrawDao.findFirstByUserIdAndDrawId(userId, drawId)
    }

    override fun save(userId: String, drawId: String): FocusDraw {
        val focusDraw = FocusDraw()
        focusDraw.userId = userId
        focusDraw.drawId = drawId
        return focusDrawDao.save(focusDraw)
    }

    override fun remove(userId: String, drawId: String): Boolean {
        try {
            focusDrawDao.deleteByUserIdAndDrawId(userId, drawId)
            return true
        } catch (e: Exception) {
            throw e
        }
    }

    override fun countByDrawId(drawId: String): Long {
        return focusDrawDao.countByDrawId(drawId)
    }
}