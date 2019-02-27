package com.junjie.secdraservice.dao

import com.junjie.secdraservice.model.Tag
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor

interface ITagDao : JpaRepository<Tag, String>, JpaSpecificationExecutor<Tag> {
    //    fun findFirst(@Nullable specification: Specification<Tag>): Tag
}