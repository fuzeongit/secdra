package com.junjie.secdradata.database.primary.dao

import com.junjie.secdradata.database.primary.entity.SpecialCode
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface SpecialCodeDAO : JpaRepository<SpecialCode, String> {
    fun findOneByCode(code: String): Optional<SpecialCode>
}