package com.junjie.secdraservice.serviceimpl

import com.junjie.secdracore.exception.NotFoundException
import com.junjie.secdradata.database.primary.dao.AuthorizeCodeDAO
import com.junjie.secdradata.database.primary.entity.AuthorizeCode
import com.junjie.secdraservice.service.AuthorizeCodeService
import org.springframework.stereotype.Service

@Service
class AuthorizeCodeServiceImpl(private val authorizeCodeDAO: AuthorizeCodeDAO) : AuthorizeCodeService {
    override fun getByCode(code: String): AuthorizeCode {
        return authorizeCodeDAO.findOneByCode(code).orElseThrow { NotFoundException("特殊操作码不存在") }
    }

    override fun save(code: String): AuthorizeCode {
        return authorizeCodeDAO.save(AuthorizeCode(code))
    }

    override fun list(): List<AuthorizeCode> {
        return authorizeCodeDAO.findAll()
    }

    override fun remove(id: String): Boolean {
        authorizeCodeDAO.deleteById(id)
        return true
    }

}