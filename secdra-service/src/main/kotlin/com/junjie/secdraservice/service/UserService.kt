package com.junjie.secdraservice.service

import com.junjie.secdracore.exception.ProgramException
import com.junjie.secdraservice.dao.IUserDao
import com.junjie.secdraservice.model.User
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserService(val userDao: IUserDao) : IUserService {


    override fun register(user: User): User {
        if (userDao.existsByPhone(user.phone!!)) {
            throw ProgramException("手机号已存在", 403)
        }
        return userDao.save(user)
    }

    override fun login(phone: String, password: String): User {
        if (!userDao.existsByPhone(phone)) {
            throw ProgramException("手机号不存在", 403)
        }
        return userDao.findOnByPhoneAndPassword(phone, password).orElseThrow { ProgramException("账号密码不正确", 401) }
    }

    override fun rePassword(phone: String, password: String, rePasswordTime: Date): User {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getInfo(id: String): User {
        return userDao.findById(id).orElseThrow { ProgramException("用户信息不存在", 401) }
    }

    override fun updateInfo(user: User): User {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}





