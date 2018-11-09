package com.junjie.secdraservice.service

import com.junjie.secdraservice.model.Draw
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.*

/**
 * 画的服务
 *
 * @author fjj
 */
interface IDrawService {
    fun paging(pageable: Pageable, name: String?, startDate: Date?, endDate: Date?): Page<Draw>

    fun pagingByUserId(pageable: Pageable, userId: String, startDate: Date?, endDate: Date?, isSelf: Boolean): Page<Draw>

    fun get(id: String, userId: String?): Draw

    fun update(userId: String, drawId: String, introduction: String?, isPrivate: Boolean = false): Draw

    fun save(draw: Draw): Draw

    fun findRand(): List<Draw>
}