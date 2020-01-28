package com.junjie.secdradata.database.primary.dao

import com.junjie.secdradata.database.primary.entity.Footprint
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Repository
interface FootprintDAO : JpaRepository<Footprint, String>, JpaSpecificationExecutor<Footprint> {
    fun getFirstByUserIdAndPictureId(userId: String, pictureId: String): Optional<Footprint>

    fun existsByUserIdAndPictureId(userId: String, pictureId: String): Boolean

    @Transactional
    fun deleteByUserIdAndPictureId(userId: String, pictureId: String)

    fun countByPictureId(pictureId: String): Long

    fun findAllByUserId(userId: String, pageable: Pageable): Page<Footprint>

    fun findAllByPictureId(pictureId: String, pageable: Pageable): Page<Footprint>
}