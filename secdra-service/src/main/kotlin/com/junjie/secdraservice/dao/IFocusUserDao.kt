package com.junjie.secdraservice.dao

import com.junjie.secdraservice.model.FocusUser
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.transaction.annotation.Transactional

interface IFocusUserDao : JpaRepository<FocusUser, String>{
    fun existsByUserIdAndFocusUserId(userId: String, focusUserId: String): Boolean
    @Transactional
    fun deleteByUserIdAndFocusUserId(userId: String, focusUserId: String)
}