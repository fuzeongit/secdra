package com.junjie.secdraservice.dao

import com.junjie.secdraservice.model.Tag
import org.springframework.data.domain.Page
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.lang.Nullable
import java.util.*

interface ITagDao : JpaRepository<Tag, String>, JpaSpecificationExecutor<Tag> {
    //    fun findFirst(@Nullable specification: Specification<Tag>): Tag
}