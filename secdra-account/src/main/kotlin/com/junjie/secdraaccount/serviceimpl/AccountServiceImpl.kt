package com.junjie.secdraaccount.serviceimpl

import com.junjie.secdraaccount.service.AccountService
import com.junjie.secdracore.exception.NotFoundException
import com.junjie.secdracore.exception.PermissionException
import com.junjie.secdracore.exception.SignInException
import com.junjie.secdradata.database.primary.entity.User
import com.junjie.secdradata.database.account.dao.AccountDAO
import com.junjie.secdradata.database.account.entity.Account
import org.springframework.stereotype.Service
import java.util.*

@Service
class AccountServiceImpl(private val accountDAO: AccountDAO) : AccountService {
    override fun get(id: String): Account {
        return accountDAO.findById(id).orElseThrow { NotFoundException("账号不存在") }
    }

    override fun existsByPhone(phone: String): Boolean {
        return accountDAO.existsByPhone(phone)
    }

    override fun signUp(phone: String, password: String, rePasswordDate: Date): Account {
        existsByPhone(phone) && throw PermissionException("手机号已存在")
        val account = Account(phone, password, rePasswordDate)
        return accountDAO.save(account)
    }

    override fun signIn(phone: String, password: String): Account {
        !existsByPhone(phone) && throw PermissionException("手机号不存在")
        return accountDAO.findOneByPhoneAndPassword(phone, password).orElseThrow { SignInException("账号密码不正确") }
    }

    override fun forgot(phone: String, password: String, rePasswordTime: Date): User {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}