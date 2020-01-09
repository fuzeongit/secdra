package com.junjie.secdraservice.service

import com.junjie.secdradata.index.primary.document.DrawDocument
import com.junjie.secdradata.database.primary.entity.Draw
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

    @Deprecated("由于ES的引入，弃用改查询，现在暂时按数据库直接出，加了足迹功能后会写推荐")
    fun pagingRand(pageable: Pageable): Page<Draw>

    @Deprecated("由于ES的引入，弃用改查询，使用ES的countByTag")
    fun countByTag(tag: String): Long

    fun get(id: String): Draw

    fun remove(id: String): Boolean

    fun list(): List<Draw>

    fun listByUserId(userId: String): List<Draw>

    fun save(draw: Draw): DrawDocument

    fun synchronizationIndexDraw(): Long
}