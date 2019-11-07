package com.junjie.secdracollect.service

import com.junjie.secdradata.constant.TransferState
import com.junjie.secdradata.database.collect.entity.AccountToPixivUser
import com.junjie.secdradata.database.collect.entity.PixivDraw

interface PixivDrawService {
    fun save(pixivDraw: PixivDraw): PixivDraw

    fun listByState(state: TransferState): List<PixivDraw>

    fun listByPixivId(pixivId: String): List<PixivDraw>

    fun existsAccountByPixivUserId(pixivUserId: String): Boolean

    fun getAccountByPixivUserId(pixivUserId: String): AccountToPixivUser

    fun saveAccount(accountId: String, pixivUserId: String): AccountToPixivUser

    fun getByDrawId(drawId: String): PixivDraw
}