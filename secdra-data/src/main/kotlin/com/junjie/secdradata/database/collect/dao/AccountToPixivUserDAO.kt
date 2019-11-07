package com.junjie.secdradata.database.collect.dao

import com.junjie.secdradata.database.collect.entity.AccountToPixivUser
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface AccountToPixivUserDAO : JpaRepository<AccountToPixivUser, String> {
    fun existsByAccountId(accountId: String): Boolean

    fun existsByPixivUserId(pixivUserId: String): Boolean

    fun findOneByPixivUserId(pixivUserId: String): Optional<AccountToPixivUser>
}