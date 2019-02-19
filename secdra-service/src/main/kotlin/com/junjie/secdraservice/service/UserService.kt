package com.junjie.secdraservice.service

import com.junjie.secdracore.exception.PermissionException
import com.junjie.secdracore.exception.SignInException
import com.junjie.secdraservice.dao.IUserDao
import com.junjie.secdraservice.model.User
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserService(val userDao: IUserDao) : IUserService {


    override fun save(user: User): User {
        return userDao.save(user)
    }

    override fun register(user: User): User {
        if (userDao.existsByPhone(user.phone!!)) {
            throw PermissionException("手机号已存在")
        }
        return userDao.save(user)
    }

    override fun login(phone: String, password: String): User {
        if (!userDao.existsByPhone(phone)) {
            throw PermissionException("手机号不存在")
        }
        return userDao.findOneByPhoneAndPassword(phone, password).orElseThrow { SignInException("账号密码不正确") }
    }

    override fun rePassword(phone: String, password: String, rePasswordTime: Date): User {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    @Cacheable("userInfo",key="#id")
    override fun getInfo(id: String): User {
        return userDao.findById(id).orElseThrow { PermissionException("用户信息不存在") }
    }

    override fun getInfoByDrawId(drawId: String): User {
        return userDao.findByDrawId(drawId).orElseThrow { PermissionException("用户信息不存在") }
    }

    override fun updateInfo(user: User): User {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}





