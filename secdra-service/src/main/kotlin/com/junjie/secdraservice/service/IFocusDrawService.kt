package com.junjie.secdraservice.service

import com.junjie.secdraservice.model.FocusDraw
import org.springframework.stereotype.Service

/**
 * 画的收藏服务
 *
 * @author fjj
 */

interface IFocusDrawService {
    fun exists(userId: String, drawId: String): Boolean

    fun get(userId: String, drawId: String): FocusDraw

    fun save(userId: String, drawId: String): FocusDraw

    fun remove(userId: String, drawId: String): Boolean

    fun countByDrawId(drawId: String):Long
}