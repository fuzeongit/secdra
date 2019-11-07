package com.junjie.secdraadmin.code.communal

import com.junjie.secdraaccount.service.AccountService
import com.junjie.secdraadmin.constant.UserConstant
import com.junjie.secdradata.constant.Gender
import com.junjie.secdradata.database.primary.entity.User
import com.junjie.secdraservice.service.UserService
import java.util.*

abstract class CommonAbstract {
    abstract val accountService: AccountService

    abstract val userService: UserService

    fun initUser(): User {
        val phoneRange = (0..13999999999)
        var phone = phoneRange.shuffled().last()
        while (accountService.existsByPhone(phone.toString())) {
            phone = phoneRange.shuffled().last()
        }
        val account = accountService.signUp(phone.toString(), "123456", Date())
        val gender = if (phone % 2 == 0.toLong()) Gender.FEMALE else Gender.MALE
        val user = User(accountId = account.id!!, gender = gender, name = UserConstant.nameList.shuffled().last())
        return userService.save(user)
    }
}