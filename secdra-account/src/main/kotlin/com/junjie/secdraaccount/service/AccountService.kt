package com.junjie.secdraaccount.service

import com.junjie.secdradata.database.primary.entity.User
import com.junjie.secdradata.database.account.entity.Account
import java.util.*

interface AccountService {
    fun get(id: String): Account


    fun getByPhone(phone: String): Account
    /**
     * 是否存在手机
     */
    fun existsByPhone(phone: String): Boolean

    /**
     * 注册
     */
    fun signUp(phone: String, password: String, rePasswordDate: Date): Account

    /**
     * 登录
     */
    fun signIn(phone: String, password: String): Account

    /**
     * 修改密码
     */
    fun forgot(phone: String, password: String, rePasswordTime: Date): User
}