package com.junjie.secdraservice.service

import com.junjie.secdraservice.model.Collection
import com.junjie.secdraservice.model.Footprint
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface FootprintService {
    fun get(userId: String, drawId: String): Footprint

    fun exists(userId: String, drawId: String): Boolean

    fun save(userId: String, drawId: String): Footprint

    fun update(userId: String, drawId: String): Footprint

    fun remove(userId: String, drawId: String): Boolean

    fun countByDrawId(drawId: String): Long

    fun pagingByUserId(userId:String, pageable: Pageable): Page<Footprint>

    fun pagingByDrawId(drawId: String, pageable: Pageable): Page<Footprint>
}