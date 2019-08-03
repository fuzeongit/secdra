package com.junjie.secdraservice.service

import com.junjie.secdracore.constant.CollectState
import com.junjie.secdraservice.model.Collection
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable


/**
 * 画的收藏服务
 *
 * @author fjj
 */

interface CollectionService {
    fun exists(userId: String, drawId: String): CollectState

    fun get(userId: String, drawId: String): Collection

    fun save(userId: String, drawId: String): Collection

    fun remove(userId: String, drawId: String): Boolean

    fun countByDrawId(drawId: String):Long

    fun paging(userId:String, pageable: Pageable): Page<Collection>
}