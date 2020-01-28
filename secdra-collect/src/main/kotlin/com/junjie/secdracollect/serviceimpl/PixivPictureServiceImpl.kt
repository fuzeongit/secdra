package com.junjie.secdracollect.serviceimpl

import com.junjie.secdracollect.service.PixivPictureService
import com.junjie.secdracore.exception.NotFoundException
import com.junjie.secdradata.constant.TransferState
import com.junjie.secdradata.database.collect.dao.AccountToPixivUserDAO
import com.junjie.secdradata.database.collect.dao.PixivPictureDAO
import com.junjie.secdradata.database.collect.entity.AccountToPixivUser
import com.junjie.secdradata.database.collect.entity.PixivPicture
import org.springframework.stereotype.Service

@Service
class PixivPictureServiceImpl(
        private val pixivPictureDAO: PixivPictureDAO,
        private val accountToPixivUserDAO: AccountToPixivUserDAO
) : PixivPictureService {

    override fun save(pixivPicture: PixivPicture): PixivPicture {
        return pixivPictureDAO.save(pixivPicture)
    }

    override fun listByState(state: TransferState): List<PixivPicture> {
        return pixivPictureDAO.findAllByState(state)
    }

    override fun listByPixivId(pixivId: String): List<PixivPicture> {
        return pixivPictureDAO.findAllByPixivId(pixivId)
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

    override fun getByPictureId(PictureId: String): PixivPicture {
        return pixivPictureDAO.findOneByPictureId(PictureId).orElseThrow { NotFoundException("采集图片不存在") }
    }
}