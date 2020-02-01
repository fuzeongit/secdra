package com.junjie.secdracollect.service

import com.junjie.secdradata.constant.TransferState
import com.junjie.secdradata.database.collect.entity.AccountToPixivUser
import com.junjie.secdradata.database.collect.entity.PixivPicture

interface PixivPictureService {
    fun save(pixivPicture: PixivPicture): PixivPicture

    fun saveAll(pixivPictureList: List<PixivPicture>): List<PixivPicture>

    fun listByState(state: TransferState): List<PixivPicture>

    fun listByPixivId(pixivId: String): List<PixivPicture>

    fun existsAccountByPixivUserId(pixivUserId: String): Boolean

    fun getAccountByPixivUserId(pixivUserId: String): AccountToPixivUser

    fun saveAccount(accountId: String, pixivUserId: String): AccountToPixivUser

    fun getByPictureId(PictureId: String): PixivPicture
}