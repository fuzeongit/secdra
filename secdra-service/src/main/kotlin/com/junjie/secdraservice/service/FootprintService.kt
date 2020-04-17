package com.junjie.secdraservice.service

import com.junjie.secdradata.database.primary.entity.Footprint
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface FootprintService {
    fun get(userId: String, pictureId: String): Footprint

    fun exists(userId: String, pictureId: String): Boolean

    fun save(pictureId: String): Footprint

    fun update(userId: String, pictureId: String): Footprint

    fun remove(userId: String, pictureId: String): Boolean

    fun countByPictureId(pictureId: String): Long

    fun pagingByUserId(userId: String, pageable: Pageable): Page<Footprint>

    fun pagingByPictureId(pictureId: String, pageable: Pageable): Page<Footprint>
}