package com.junjie.secdraservice.dao

import com.junjie.secdraservice.model.FocusDraw
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.transaction.annotation.Transactional

interface IFocusDrawDao : JpaRepository<FocusDraw, String> {
    fun existsByUserIdAndDrawId(userId: String, drawId: String): Boolean
    @Transactional
    fun deleteByUserIdAndDrawId(userId: String, drawId: String)

    fun findFirstByUserIdAndDrawId(userId: String, drawId: String): FocusDraw

    fun countByDrawId(drawId: String): Long
}