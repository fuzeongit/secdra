package com.junjie.secdraservice.dao

import com.junjie.secdraservice.model.Draw
import com.junjie.secdraservice.model.Tag
import io.lettuce.core.dynamic.annotation.Param
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query

interface ITagDao : JpaRepository<Tag, String>, JpaSpecificationExecutor<Tag> {
//    @Query("select t.* fROM tag as t INNER JOIN draw as d t.draw_id = d.id group by t.name order by sum(d.likeAmount)")
//    fun findByTag(@Param("introduction") introduction: String): List<Draw>
}