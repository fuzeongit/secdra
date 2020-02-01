package com.junjie.secdracollect.serviceimpl

import com.junjie.secdracollect.service.PixivErrorService
import com.junjie.secdracore.exception.NotFoundException
import com.junjie.secdradata.database.collect.dao.PixivErrorDAO
import com.junjie.secdradata.database.collect.entity.PixivError
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class PixivErrorServiceImpl(private val pixivErrorDAO: PixivErrorDAO) : PixivErrorService {
    override fun get(id: String): PixivError = pixivErrorDAO.findById(id).orElseThrow { throw NotFoundException("错误记录不存在") }

    override fun listByRecord(record: Boolean, status: Int?): List<PixivError> {
        return if (status == null) pixivErrorDAO.findAllByRecord(record)
        else pixivErrorDAO.findAllByStatusAndRecord(status, record)
    }

    override fun save(pixivError: PixivError): PixivError {
        return pixivErrorDAO.save(pixivError)
    }
}