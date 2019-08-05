package com.junjie.secdraservice.service

import com.junjie.secdraservice.model.User
import java.util.*

/**
 * 用户的服务
 *
 * @author fjj
 */
interface UserService {
    /**
     * 保存
     */
    fun save(user: User): User

    /**
     * 是否存在手机
     */
    fun existsByPhone(phone: String): Boolean

    /**
     * 注册
     */
    fun register(phone:String,password: String,rePasswordDate: Date): User

    /**
     * 登录
     */
    fun login(phone: String, password: String): User


    /**
     * 修改密码
     */
    fun rePassword(phone: String, password: String, rePasswordTime: Date): User

    /**
     * 获取用户信息
     */
    fun getInfo(id: String): User

    /**
     * 根据画的id获取用户信息
     */
    fun getInfoByDrawId(drawId: String): User

    /**
     * 修改用户信息
     */
    fun updateInfo(user: User): User
}
