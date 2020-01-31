package com.junjie.secdraservice.service

import com.junjie.secdradata.database.primary.entity.SpecialCode

interface SpecialCodeService {
    fun getByCode(code: String): SpecialCode

    fun save(code: String): SpecialCode
}