package com.junjie.secdracollect.dao

import com.junjie.secdracollect.model.PixivError
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PixivErrorDAO : JpaRepository<PixivError, String> {
    fun findAllByPixivId(pixivId: String): List<PixivError>
}