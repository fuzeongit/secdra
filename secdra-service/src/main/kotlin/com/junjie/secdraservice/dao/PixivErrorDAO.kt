package com.junjie.secdraservice.dao

import com.junjie.secdraservice.model.PixivError
import org.springframework.data.jpa.repository.JpaRepository


interface PixivErrorDAO : JpaRepository<PixivError, String> {

}