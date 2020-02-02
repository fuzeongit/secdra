package com.junjie.secdradata.database.primary.dao

import com.junjie.secdradata.database.primary.entity.AuthorizeCode
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface AuthorizeCodeDAO : JpaRepository<AuthorizeCode, String> {
    fun findOneByCode(code: String): Optional<AuthorizeCode>
}