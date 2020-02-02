package com.junjie.secdraservice.service

import com.junjie.secdradata.database.primary.entity.AuthorizeCode

interface AuthorizeCodeService {
    fun getByCode(code: String): AuthorizeCode

    fun save(code: String): AuthorizeCode

    fun list(): List<AuthorizeCode>

    fun remove(id: String): Boolean
}