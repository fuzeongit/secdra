package com.junjie.secdradata.database.collect.dao

import com.junjie.secdradata.constant.TransferState
import com.junjie.secdradata.database.collect.entity.PixivPicture
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface PixivPictureDAO : JpaRepository<PixivPicture, String> {
    fun findAllByPixivId(pixivId: String): List<PixivPicture>

    fun findAllByState(state: TransferState): List<PixivPicture>

    fun findOneByPictureId(pictureId: String): Optional<PixivPicture>
}