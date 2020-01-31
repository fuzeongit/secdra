package com.junjie.secdradata.database.account.dao

import com.junjie.secdradata.database.account.entity.Account
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository

interface AccountDAO : JpaRepository<Account, String> {
    fun existsByPhone(phone: String): Boolean

    fun findOneByPhone(phone: String): Optional<Account>

    fun findOneByPhoneAndPassword(phone: String, password: String): Optional<Account>

    fun findAllByPhoneLike(phone: String): List<Account>
}