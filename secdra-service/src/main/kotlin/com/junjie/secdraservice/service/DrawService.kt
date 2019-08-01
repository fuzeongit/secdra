package com.junjie.secdraservice.service

import com.junjie.secdrasearch.model.DrawDocument
import com.junjie.secdraservice.model.Draw
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.*

/**
 * 画的服务
 *
 * @author fjj
 */
interface DrawService {
    fun paging(pageable: Pageable, tag: String?, startDate: Date?, endDate: Date?): Page<Draw>

    fun pagingByUserId(pageable: Pageable, userId: String, startDate: Date?, endDate: Date?, isSelf: Boolean): Page<Draw>

    fun get(id: String): Draw

    fun update(id: String, viewAmount: Long?, likeAmount: Long?): Draw

    fun save(draw: Draw): Draw

    fun pagingRand(pageable: Pageable): Page<Draw>

    fun getFirstByTag(tag: String): Draw

    fun countByTag(tag: String): Long

    fun synchronizationIndexDraw(): Long

    fun paging(pageable: Pageable, tag: String): Page<DrawDocument>
}