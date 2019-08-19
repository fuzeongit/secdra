package com.junjie.secdraservice.serviceimpl

import com.junjie.secdracore.exception.PermissionException
import com.junjie.secdracore.exception.SignInException
import com.junjie.secdraservice.dao.UserDAO
import com.junjie.secdraservice.model.User
import com.junjie.secdraservice.service.UserService
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserServiceImpl(val userDAO: UserDAO) : UserService {
    @CachePut("user::getInfo", key = "#user.id")
    override fun save(user: User): User {
        return userDAO.save(user)
    }

    override fun existsByPhone(phone: String): Boolean {
        return userDAO.existsByPhone(phone)
    }

    override fun register(phone: String, password: String, rePasswordDate: Date): User {
        if (existsByPhone(phone)) {
            throw PermissionException("手机号已存在")
        }
        val user = User(phone, password, rePasswordDate)
        return userDAO.save(user)
    }

    override fun login(phone: String, password: String): User {
        if (!existsByPhone(phone)) {
            throw PermissionException("手机号不存在")
        }
        return userDAO.findOneByPhoneAndPassword(phone, password).orElseThrow { SignInException("账号密码不正确") }
    }


    override fun rePassword(phone: String, password: String, rePasswordTime: Date): User {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    @Cacheable("user::getInfo")
    override fun getInfo(id: String): User {
        return userDAO.findById(id).orElseThrow { PermissionException("用户信息不存在") }
    }

    override fun getInfoByDrawId(drawId: String): User {
        return userDAO.findByDrawId(drawId).orElseThrow { PermissionException("用户信息不存在") }
    }

    override fun updateInfo(user: User): User {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}





