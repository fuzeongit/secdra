package com.junjie.secdraservice.dao

import com.junjie.secdraservice.model.Draw
import org.springframework.data.jpa.repository.JpaRepository

interface IDrawDao : JpaRepository<Draw, String> {

}