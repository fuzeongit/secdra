package com.junjie.secdraservice.service

import com.junjie.secdraservice.document.DrawDocument
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
    @Deprecated("由于ES的引入，弃用改查询，查询的时候使用ES查询")
    fun paging(pageable: Pageable, tag: String?, startDate: Date?, endDate: Date?): Page<Draw>

    @Deprecated("由于ES的引入，弃用改查询，以合并到ES的piging")
    fun pagingByUserId(pageable: Pageable, userId: String, startDate: Date?, endDate: Date?, isSelf: Boolean): Page<Draw>

    fun get(id: String): Draw

    fun update(id: String, viewAmount: Long?, likeAmount: Long?): DrawDocument

    fun save(draw: Draw): DrawDocument

    fun pagingRand(pageable: Pageable): Page<Draw>

    fun getFirstByTag(tag: String): Draw

    fun countByTag(tag: String): Long

    fun synchronizationIndexDraw(): Long
}