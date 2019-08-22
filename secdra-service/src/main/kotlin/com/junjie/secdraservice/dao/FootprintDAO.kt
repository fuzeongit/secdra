package com.junjie.secdraservice.dao

import com.junjie.secdraservice.model.Collection
import com.junjie.secdraservice.model.Footprint
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Repository
interface FootprintDAO : JpaRepository<Footprint, String>, JpaSpecificationExecutor<Footprint> {
    fun getFirstByUserIdAndDrawId(userId: String, drawId: String): Optional<Footprint>

    fun existsByUserIdAndDrawId(userId: String, drawId: String): Boolean

    @Transactional
    fun deleteByUserIdAndDrawId(userId: String, drawId: String)

    fun countByDrawId(drawId: String): Long

    fun findAllByUserId(userId: String, pageable: Pageable): Page<Footprint>

    fun findAllByDrawId(drawId: String, pageable: Pageable): Page<Footprint>
}