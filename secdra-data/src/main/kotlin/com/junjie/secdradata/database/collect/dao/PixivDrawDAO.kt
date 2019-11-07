package com.junjie.secdradata.database.collect.dao

import com.junjie.secdradata.constant.TransferState
import com.junjie.secdradata.database.collect.entity.PixivDraw
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface PixivDrawDAO : JpaRepository<PixivDraw, String> {
    fun findAllByPixivId(pixivId: String): List<PixivDraw>

    fun findAllByState(state: TransferState): List<PixivDraw>

    fun findOneByDrawId(drawId: String): Optional<PixivDraw>
}