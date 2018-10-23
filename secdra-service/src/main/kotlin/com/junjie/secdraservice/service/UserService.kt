package com.junjie.secdraservice.service

import com.junjie.secdracore.exception.ProgramException
import com.junjie.secdraservice.dao.IUserDao
import com.junjie.secdraservice.model.User
import org.springframework.data.domain.Example
import org.springframework.stereotype.Service

@Service
class UserService(val userDao: IUserDao):IUserService {
    override fun register(phone: String, password: String): User {
        if(userDao.existsByPhone(phone)){
            throw ProgramException("手机号已存在", 403)
        }
        val user = User()
        user.phone = phone;
        user.password = password;
        return userDao.save(user)
    }

    override fun login(phone: String, password: String): User {
        if (!userDao.existsByPhone(phone)) {
            throw ProgramException("手机号不存在", 403)
        }
        return userDao.findOnByPhoneAndPassword(phone,password).orElseThrow { ProgramException("账号密码不正确", 401) }
    }

    override fun getInfo(id: String): User {
        return userDao.findById(id).orElseThrow {ProgramException("用户信息不存在",404)}
    }
}





