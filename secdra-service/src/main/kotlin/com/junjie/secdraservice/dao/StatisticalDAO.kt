package com.junjie.secdraservice.dao

import com.junjie.secdraservice.model.Statistical
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface StatisticalDAO : JpaRepository<Statistical, String> {

}