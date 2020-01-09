package com.junjie.secdraservice.service

import com.junjie.secdradata.constant.CollectState
import com.junjie.secdradata.database.primary.entity.Collection
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable


/**
 * 画的收藏服务
 *
 * @author fjj
 */

interface CollectionService {
    fun exists(userId: String?, drawId: String): CollectState

    fun save(userId: String, drawId: String): Collection

    fun remove(userId: String, drawId: String): Boolean

    fun countByDrawId(drawId: String): Long

    fun pagingByUserId(userId: String, pageable: Pageable): Page<Collection>

    fun pagingByDrawId(drawId: String, pageable: Pageable): Page<Collection>
}