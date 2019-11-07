package com.junjie.secdracollect.serviceimpl

import com.junjie.secdracollect.service.PixivDrawService
import com.junjie.secdracore.exception.NotFoundException
import com.junjie.secdradata.constant.TransferState
import com.junjie.secdradata.database.collect.dao.AccountToPixivUserDAO
import com.junjie.secdradata.database.collect.dao.PixivDrawDAO
import com.junjie.secdradata.database.collect.entity.AccountToPixivUser
import com.junjie.secdradata.database.collect.entity.PixivDraw
import org.springframework.stereotype.Service

@Service
class PixivDrawServiceImpl(
        private val pixivDrawDAO: PixivDrawDAO,
        private val accountToPixivUserDAO: AccountToPixivUserDAO
) : PixivDrawService {

    override fun save(pixivDraw: PixivDraw): PixivDraw {
        return pixivDrawDAO.save(pixivDraw)
    }

    override fun listByState(state: TransferState): List<PixivDraw> {
        return pixivDrawDAO.findAllByState(state)
    }

    override fun listByPixivId(pixivId: String): List<PixivDraw> {
        return pixivDrawDAO.findAllByPixivId(pixivId)
    }

    override fun existsAccountByPixivUserId(pixivUserId: String): Boolean {
        return accountToPixivUserDAO.existsByPixivUserId(pixivUserId)
    }

    override fun getAccountByPixivUserId(pixivUserId: String): AccountToPixivUser {
        return accountToPixivUserDAO.findOneByPixivUserId(pixivUserId).orElseThrow { NotFoundException("用户不存在") }
    }

    override fun saveAccount(accountId: String, pixivUserId: String): AccountToPixivUser {
        return accountToPixivUserDAO.save(AccountToPixivUser(accountId, pixivUserId))
    }

    override fun getByDrawId(drawId: String): PixivDraw {
        return pixivDrawDAO.findOneByDrawId(drawId).orElseThrow { NotFoundException("采集图片不存在") }
    }
}