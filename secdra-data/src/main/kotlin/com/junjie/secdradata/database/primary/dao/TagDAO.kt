package com.junjie.secdradata.database.primary.dao

import com.junjie.secdradata.database.primary.entity.Tag
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository

@Repository
interface TagDAO : JpaRepository<Tag, String>, JpaSpecificationExecutor<Tag>