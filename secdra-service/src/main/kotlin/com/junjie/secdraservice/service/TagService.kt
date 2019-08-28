package com.junjie.secdraservice.service

import com.junjie.secdradata.database.primary.entity.Tag

interface TagService {
    @Deprecated("由于ES的引入，弃用改查询，使用ES的listTag")
    fun listTagOrderByLikeAmount(): List<Tag>
}