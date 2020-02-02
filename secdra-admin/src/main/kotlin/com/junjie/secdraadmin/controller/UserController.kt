package com.junjie.secdraadmin.controller

import com.junjie.secdraaccount.service.AccountService
import com.junjie.secdraadmin.core.communal.CommonAbstract
import com.junjie.secdraadmin.constant.UserConstant
import com.junjie.secdraadmin.vo.UserListVO
import com.junjie.secdracollect.service.PixivPictureService
import com.junjie.secdracore.annotations.RestfulPack
import com.junjie.secdradata.constant.Gender
import com.junjie.secdradata.constant.TransferState
import com.junjie.secdradata.database.primary.entity.User
import com.junjie.secdraservice.service.UserService
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.GetMapping
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
    @GetMapping("paging")
    @RestfulPack
    fun paging(@PageableDefault(value = 20) pageable: Pageable, name: String?, phone: String?): Page<UserListVO> {
        val accountIdList =
                if (phone != null && phone.isNotEmpty()) accountService.listByPhoneLike(phone).map { it.id!! }
                else listOf()
        return getPageVO(userService.paging(pageable, name, accountIdList))
    }

    /**
     * 注册一个账号
     */
    @PostMapping("signUp")
    @RestfulPack
    fun signUp(phone: String): User {
        val account = accountService.signUp(phone, "123456")
        val user = User(accountId = account.id!!, gender = Gender.FEMALE, name = UserConstant.nameList.shuffled().last())
        return userService.save(user)
    }

    /**
     * 按名称获取列表
     */
    @GetMapping("list")
    @RestfulPack
    fun list(name: String?) = getUserListVO(userService.list(name))

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

    private fun getUserListVO(userList: List<User>): List<UserListVO> {
        return userList.map {
            UserListVO(it, accountService.get(it.accountId).phone)
        }
    }

    private fun getPageVO(page: Page<User>): Page<UserListVO> {
        val pictureVOList = getUserListVO(page.content)
        return PageImpl(pictureVOList, page.pageable, page.totalElements)
    }
}