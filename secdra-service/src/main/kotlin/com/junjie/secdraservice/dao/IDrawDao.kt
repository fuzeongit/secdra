package com.junjie.secdraservice.dao

import com.junjie.secdraservice.model.Draw
import com.junjie.secdraservice.model.Tag
import io.lettuce.core.dynamic.annotation.Param
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query

interface IDrawDao : JpaRepository<Draw, String>, JpaSpecificationExecutor<Draw> {
    fun findAllByUserId(pageable: Pageable): Page<Draw>
}