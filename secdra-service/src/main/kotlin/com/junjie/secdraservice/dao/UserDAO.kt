package com.junjie.secdraservice.dao

import com.junjie.secdraservice.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserDAO : JpaRepository<User, String> {
    fun existsByPhone(phone: String): Boolean

    fun findOneByPhoneAndPassword(phone: String, password: String): Optional<User>

    @Query("SELECT `USER`.* FROM `USER` JOIN draw ON `USER`.id = draw.user_id WHERE draw.id = ?",nativeQuery = true)
    fun findByDrawId(drawId: String): Optional<User>
}