package com.junjie.secdraservice.service

import com.junjie.secdraservice.model.Collection

/**
 * 画的收藏服务
 *
 * @author fjj
 */

interface ICollectionService {
    fun exists(userId: String, drawId: String): Boolean

    fun get(userId: String, drawId: String): Collection

    fun save(userId: String, drawId: String): Collection

    fun remove(userId: String, drawId: String): Boolean

    fun countByDrawId(drawId: String):Long
}