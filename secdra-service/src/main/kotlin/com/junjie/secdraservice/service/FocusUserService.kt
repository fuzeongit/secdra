package com.junjie.secdraservice.service

import com.junjie.secdraservice.dao.IFocusUserDao
import com.junjie.secdraservice.model.FocusDraw
import com.junjie.secdraservice.model.FocusUser
import org.springframework.stereotype.Service

@Service
class FocusUserService(private val focusUserDao: IFocusUserDao) : IFocusUserService {
    override fun exists(userId: String?, focusUserId: String): Boolean? {
        if (userId == focusUserId) {
            return null
        }
        return try {
            focusUserDao.existsByUserIdAndFocusUserId(userId!!, focusUserId)
        } catch (e: Exception) {
            null
        }
    }

    override fun save(userId: String, focusUserId: String): FocusUser {
        val focusUser = FocusUser()
        focusUser.userId = userId
        focusUser.focusUserId = focusUserId
        return focusUserDao.save(focusUser)
    }

    override fun remove(userId: String, focusUserId: String): Boolean {
        try {
            focusUserDao.deleteByUserIdAndFocusUserId(userId, focusUserId)
            return true
        } catch (e: Exception) {
            throw e
        }
    }
}
