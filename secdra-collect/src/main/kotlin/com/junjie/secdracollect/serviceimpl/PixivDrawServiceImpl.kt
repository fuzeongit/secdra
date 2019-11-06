package com.junjie.secdracollect.serviceimpl

import com.junjie.secdracollect.service.PixivDrawService
import com.junjie.secdradata.constant.TransferState
import com.junjie.secdradata.database.collect.dao.PixivDrawDAO
import com.junjie.secdradata.database.collect.entity.PixivDraw
import org.springframework.stereotype.Service

@Service
class PixivDrawServiceImpl(private val pixivDrawDAO: PixivDrawDAO) : PixivDrawService {
    override fun save(pixivDraw: PixivDraw): PixivDraw {
        return pixivDrawDAO.save(pixivDraw)
    }

    override fun listByState(state: TransferState): List<PixivDraw> {
        return pixivDrawDAO.findAllByState(state)
    }

    override fun listByPixivId(pixivId: String): List<PixivDraw> {
        return pixivDrawDAO.findAllByPixivId(pixivId)
    }
}