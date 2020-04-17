package com.junjie.secdraweb.vo

import com.junjie.secdradata.constant.FollowState
import com.junjie.secdradata.constant.Gender
import com.junjie.secdradata.database.primary.entity.User
import org.springframework.beans.BeanUtils
import java.util.*

class UserVO(user: User) {
    lateinit var id: String

    var gender: Gender = Gender.MALE

    var birthday: Date = Date()

    lateinit var name: String

    lateinit var introduction: String

    var head: String? = null

    var background: String? = null

    var focus: FollowState = FollowState.SElF

    var country: String = "中国"

    var province: String? = null

    var city: String? = null

    init {
        BeanUtils.copyProperties(user, this)
    }
}