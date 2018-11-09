package com.junjie.secdraservice.dao

import com.junjie.secdraservice.model.Draw
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query

interface IDrawDao : JpaRepository<Draw, String>, JpaSpecificationExecutor<Draw> {
    fun findAllByUserId(pageable: Pageable): Page<Draw>

    @Query(value = "SELECT * FROM draw ORDER BY RAND()", countQuery = "SELECT count(*) FROM draw", nativeQuery = true)
    fun pagingRand(pageable: Pageable): Page<Draw>

}