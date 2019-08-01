package com.junjie.secdraservice.dao

import com.junjie.secdraservice.model.PixivDraw
import org.springframework.data.jpa.repository.JpaRepository


interface PixivDrawDAO : JpaRepository<PixivDraw, String> {
    fun findAllByPixivId(pixivId: String): List<PixivDraw>

    fun findAllByInit(init: Boolean): List<PixivDraw>
}