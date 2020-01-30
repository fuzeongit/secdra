package com.junjie.secdradata.database.primary.dao

import com.junjie.secdradata.constant.PictureLifeState
import com.junjie.secdradata.database.primary.entity.Picture
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
interface PictureDAO : JpaRepository<Picture, String>, JpaSpecificationExecutor<Picture> {
    @EntityGraph(value = "Picture.Tag", type = EntityGraph.EntityGraphType.FETCH)
    override fun findById(id: String): Optional<Picture>

    @EntityGraph(value = "Picture.Tag", type = EntityGraph.EntityGraphType.FETCH)
    fun findByIdAndLife(id: String, life: PictureLifeState): Optional<Picture>

    @EntityGraph(value = "Picture.Tag", type = EntityGraph.EntityGraphType.FETCH)
    fun findAllByLife(life: PictureLifeState): List<Picture>

    @EntityGraph(value = "Picture.Tag", type = EntityGraph.EntityGraphType.FETCH)
    override fun findAll(@Nullable specification: Specification<Picture>?, pageable: Pageable): Page<Picture>

    // @EntityGraph(value = "Picture.Tag", type = EntityGraph.EntityGraphType.FETCH)
    @Query(value = "SELECT * FROM picture ORDER BY RAND()", countQuery = "SELECT count(*) FROM picture", nativeQuery = true)
    fun pagingRand(pageable: Pageable): Page<Picture>

    fun findAllByUser_Id(userId: String): List<Picture>

    fun findAllByUser_IdAndLife(userId: String, life: PictureLifeState): List<Picture>
}