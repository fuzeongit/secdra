package com.junjie.secdradata.database.collect.dao

import com.junjie.secdradata.database.collect.entity.PixivError
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PixivErrorDAO : JpaRepository<PixivError, String> {
    fun findAllByPixivId(pixivId: String): List<PixivError>
}