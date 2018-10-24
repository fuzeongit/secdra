package com.junjie.secdraservice.service

import com.junjie.secdraservice.model.Draw
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface IDrawService {
    fun paging(pageable: Pageable): Page<Draw>

    fun get(id: String, userId: String?): Draw

    fun save(userId: String, url: String, introduction: String = "", isPrivate: Boolean = false): Draw
}