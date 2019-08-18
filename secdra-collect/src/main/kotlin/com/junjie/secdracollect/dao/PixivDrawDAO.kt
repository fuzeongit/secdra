package com.junjie.secdracollect.dao

import com.junjie.secdracollect.constant.TransferState
import com.junjie.secdracollect.model.PixivDraw
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PixivDrawDAO : JpaRepository<PixivDraw, String> {
    fun findAllByPixivId(pixivId: String): List<PixivDraw>

    fun findAllByState(state: TransferState): List<PixivDraw>
}