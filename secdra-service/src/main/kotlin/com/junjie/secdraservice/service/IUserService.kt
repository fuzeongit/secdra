package com.junjie.secdraservice.service

import com.junjie.secdraservice.model.User

interface IUserService {
    /**
     * 注册
     */
    fun register(phone: String, password: String): User

    /**
     * 登录
     */
    fun login(phone: String, password: String): User

    /**
     * 获取用户信息
     */
    fun getInfo(id: String): User
}
