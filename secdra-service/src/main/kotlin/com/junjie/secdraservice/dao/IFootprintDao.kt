package com.junjie.secdraservice.dao

import com.junjie.secdraservice.model.Footprint
import org.springframework.data.jpa.repository.JpaRepository

interface IFootprintDao : JpaRepository<Footprint, String> {
    fun findFirstByUserIdAndDrawId(userId: String, drawId: String): Footprint

    fun countByDrawId(drawId: String): Long
}