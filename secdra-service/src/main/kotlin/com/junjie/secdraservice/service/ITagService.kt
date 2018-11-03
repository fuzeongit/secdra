package com.junjie.secdraservice.service

import com.junjie.secdraservice.model.Tag

interface ITagService {
    fun listTagOrderByLikeAmount(): List<Tag>
}