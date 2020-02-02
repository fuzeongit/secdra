package com.junjie.secdraadmin.core.communal

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
        var phone = Random().nextInt(10)
        while (accountService.existsByPhone(phone.toString())) {
            phone += Random().nextInt(1000)
        }
        val account = accountService.signUp(phone.toString(), "123456")
        val gender = if (phone % 2 == 0) Gender.FEMALE else Gender.MALE
        val user = User(accountId = account.id!!, gender = gender, name = UserConstant.nameList.shuffled().last())
        return userService.save(user)
    }
}

