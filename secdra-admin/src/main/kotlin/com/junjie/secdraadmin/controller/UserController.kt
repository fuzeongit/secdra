package com.junjie.secdraadmin.controller

import com.junjie.secdraaccount.service.AccountService
import com.junjie.secdraadmin.code.communal.CommonAbstract
import com.junjie.secdraadmin.constant.UserConstant
import com.junjie.secdracollect.service.PixivDrawService
import com.junjie.secdracore.annotations.RestfulPack
import com.junjie.secdradata.constant.Gender
import com.junjie.secdradata.constant.TransferState
import com.junjie.secdradata.database.primary.dao.UserDAO
import com.junjie.secdradata.database.primary.entity.User
import com.junjie.secdraservice.service.UserService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("user")
class UserController(
        override val userService: UserService,
        override val accountService: AccountService,
        private val pixivDrawService: PixivDrawService) : CommonAbstract() {
    @PostMapping("init")
    @RestfulPack
    fun init(number: Int): ArrayList<String> {
        val phoneList = arrayListOf<String>()
        for (i in 0 until number) {
            phoneList.add(initUser().id!!)
        }
        return phoneList
    }

    @PostMapping("initByPixiv")
    @RestfulPack
    fun initByPixiv(): Boolean {
        val list = pixivDrawService.listByState(TransferState.SUCCESS)
        for (item in list) {
            if (!pixivDrawService.existsAccountByPixivUserId(item.pixivUserId!!)) {
                val user = initUser()
                pixivDrawService.saveAccount(user.accountId, item.pixivUserId!!)
            }
        }
        return true
    }
}