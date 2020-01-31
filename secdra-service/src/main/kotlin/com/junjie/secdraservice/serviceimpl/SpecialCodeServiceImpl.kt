package com.junjie.secdraservice.serviceimpl

import com.junjie.secdracore.exception.NotFoundException
import com.junjie.secdradata.database.primary.dao.SpecialCodeDAO
import com.junjie.secdradata.database.primary.entity.SpecialCode
import com.junjie.secdraservice.service.SpecialCodeService
import org.springframework.stereotype.Service

@Service
class SpecialCodeServiceImpl(private val specialCodeDAO: SpecialCodeDAO) : SpecialCodeService {
    override fun getByCode(code: String): SpecialCode {
        return specialCodeDAO.findOneByCode(code).orElseThrow { NotFoundException("特殊操作码不存在") }
    }

    override fun save(code: String): SpecialCode {
        return specialCodeDAO.save(SpecialCode(code))
    }

    override fun list(): List<SpecialCode> {
        return specialCodeDAO.findAll()
    }

    override fun remove(id: String): Boolean {
        specialCodeDAO.deleteById(id)
        return true
    }

}