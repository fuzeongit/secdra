package com.junjie.secdraservice.service

import com.junjie.secdraservice.model.Tag

interface TagService {
    fun listTagOrderByLikeAmount(): List<Tag>
}