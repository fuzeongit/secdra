package com.junjie.secdraservice.dao

import com.junjie.secdraservice.model.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface IUserDao : JpaRepository<User, String> {
    abstract fun existsByPhone(phone: String): Boolean

    abstract fun findOnByPhoneAndPassword(phone: String,password:String) :Optional<User>
}