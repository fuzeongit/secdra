package com.junjie.secdraservice.dao

import com.junjie.secdraservice.model.Statistical
import org.springframework.data.jpa.repository.JpaRepository

interface IStatisticalDao : JpaRepository<Statistical, String> {

}