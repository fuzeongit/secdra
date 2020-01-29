package com.junjie.secdraadmin.controller

import com.junjie.secdraaccount.service.AccountService
import com.junjie.secdraadmin.code.communal.CommonAbstract
import com.junjie.secdraadmin.constant.UserConstant
import com.junjie.secdracollect.service.PixivPictureService
import com.junjie.secdracore.annotations.RestfulPack
import com.junjie.secdradata.constant.Gender
import com.junjie.secdradata.constant.TransferState
import com.junjie.secdradata.database.primary.entity.User
import com.junjie.secdraservice.service.UserService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("user")
class UserController(
        override val userService: UserService,
        override val accountService: AccountService,
        private val pixivPictureService: PixivPictureService
) : CommonAbstract() {

    /**
     * 注册一个账号
     */
    @PostMapping("signUp")
    @RestfulPack
    fun signUp(phone: String): User {
        val account =  accountService.signUp(phone, "123456")
        val user = User(accountId = account.id!!, gender = Gender.FEMALE, name = UserConstant.nameList.shuffled().last())
        return userService.save(user)
    }

    /**
     * 采集成功的图片写入pixiv用户
     */
    @PostMapping("initByPixiv")
    @RestfulPack
    fun initByPixiv(): Boolean {
        val list = pixivPictureService.listByState(TransferState.SUCCESS)
        for (item in list) {
            if (!pixivPictureService.existsAccountByPixivUserId(item.pixivUserId!!)) {
                val user = initUser()
                pixivPictureService.saveAccount(user.accountId, item.pixivUserId!!)
            }
        }
        return true
    }
}