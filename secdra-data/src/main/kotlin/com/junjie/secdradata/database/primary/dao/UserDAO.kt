package com.junjie.secdradata.database.primary.dao

import com.junjie.secdradata.database.primary.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserDAO : JpaRepository<User, String>, JpaSpecificationExecutor<User> {
    fun findOneByAccountId(accountId: String): Optional<User>

    fun findAllByNameLike(name: String): List<User>
}