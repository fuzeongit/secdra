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
    fun getFirstByCreatedByAndPictureId(createdBy: String, pictureId: String): Optional<Footprint>

    fun existsByCreatedByAndPictureId(createdBy: String, pictureId: String): Boolean

    @Transactional
    fun deleteByCreatedByAndPictureId(createdBy: String, pictureId: String)

    fun countByPictureId(pictureId: String): Long

    fun findAllByCreatedBy(createdBy: String, pageable: Pageable): Page<Footprint>

    fun findAllByPictureId(pictureId: String, pageable: Pageable): Page<Footprint>
}