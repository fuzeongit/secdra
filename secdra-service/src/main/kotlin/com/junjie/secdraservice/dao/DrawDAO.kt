package com.junjie.secdraservice.dao

import com.junjie.secdraservice.model.Draw
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.lang.Nullable
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface DrawDAO : JpaRepository<Draw, String>, JpaSpecificationExecutor<Draw> {
    @EntityGraph(value = "Draw.Tag", type = EntityGraph.EntityGraphType.FETCH)
    override fun findById(id: String): Optional<Draw>

    @EntityGraph(value = "Draw.Tag", type = EntityGraph.EntityGraphType.FETCH)
    override fun findAll(@Nullable specification: Specification<Draw>?, pageable: Pageable): Page<Draw>

    // @EntityGraph(value = "Draw.Tag", type = EntityGraph.EntityGraphType.FETCH)
    @Query(value = "SELECT * FROM draw ORDER BY RAND()", countQuery = "SELECT count(*) FROM draw", nativeQuery = true)
    fun pagingRand(pageable: Pageable): Page<Draw>
}