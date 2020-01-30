package com.junjie.secdraservice.serviceimpl

import com.junjie.secdracore.exception.PermissionException
import com.junjie.secdracore.exception.SignInException
import com.junjie.secdradata.database.primary.dao.UserDAO
import com.junjie.secdradata.database.primary.entity.User
import com.junjie.secdraservice.service.UserService
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserServiceImpl(private val userDAO: UserDAO) : UserService {

    @CachePut("user::get", key = "#user.id")
    override fun save(user: User): User {
        return userDAO.save(user)
    }

    @Cacheable("user::get")
    override fun get(id: String): User {
        return userDAO.findById(id).orElseThrow { PermissionException("用户信息不存在") }
    }

    @Cacheable("user::getByAccountId")
    override fun getByAccountId(accountId: String): User {
        return userDAO.findOneByAccountId(accountId).orElseThrow { PermissionException("用户信息不存在") }
    }

    override fun list(name: String?): List<User> {
        return if (name != null && name.isNotEmpty()) {
            userDAO.findAllByNameLike("%$name%")
        } else {
            userDAO.findAll()
        }
    }

    override fun updateInfo(user: User): User {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}





