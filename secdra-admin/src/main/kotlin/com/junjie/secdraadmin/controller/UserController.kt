package com.junjie.secdraadmin.controller

import com.junjie.secdraaccount.service.AccountService
import com.junjie.secdraadmin.constant.UserConstant
import com.junjie.secdracore.annotations.RestfulPack
import com.junjie.secdradata.constant.Gender
import com.junjie.secdradata.database.primary.dao.UserDAO
import com.junjie.secdradata.database.primary.entity.User
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("user")
class UserController(private val userDAO: UserDAO, private val accountService: AccountService) {
    @PostMapping("init")
    @RestfulPack
    fun init(number: Int): ArrayList<String> {
        val phoneList = arrayListOf<String>()
        for (i in 0 until number) {
            val phone = (0..10000).shuffled().last()
            try {
                val account = accountService.signUp(phone.toString(), "123456", Date())
                val gender = if (phone % 2 == 0) Gender.FEMALE else Gender.MALE
                val user = User(accountId = account.id!!, gender = gender, name = UserConstant.nameList.shuffled().last())
                phoneList.add(userDAO.save(user).id!!)
            } catch (e: Exception) {
            }
        }
        return phoneList
    }
}