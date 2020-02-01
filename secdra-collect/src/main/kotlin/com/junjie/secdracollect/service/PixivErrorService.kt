package com.junjie.secdracollect.service

import com.junjie.secdradata.database.collect.entity.PixivError

interface PixivErrorService {
    fun save(pixivError: PixivError): PixivError

    fun listByRecord(record: Boolean, status: Int?): List<PixivError>

    fun get(id: String): PixivError
}