package com.junjie.secdracollect.serviceimpl

import com.junjie.secdracollect.service.PixivErrorService
import com.junjie.secdradata.database.collect.dao.PixivErrorDAO
import com.junjie.secdradata.database.collect.entity.PixivError
import org.springframework.stereotype.Service

@Service
class PixivErrorServiceImpl(private val pixivErrorDAO: PixivErrorDAO) : PixivErrorService {
    override fun save(pixivError: PixivError): PixivError {
        return pixivErrorDAO.save(pixivError)
    }
}