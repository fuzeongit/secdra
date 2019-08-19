package com.junjie.secdraweb.vo

import com.junjie.secdraservice.constant.FollowState
import com.junjie.secdraservice.constant.Gender
import com.junjie.secdraservice.model.User
import org.springframework.beans.BeanUtils
import java.util.*

class UserVO {
    lateinit var id: String

    lateinit var phone: String

    var gender: Gender = Gender.MALE

    var birthday: Date = Date()

    lateinit var name: String

    lateinit var introduction: String

    var address: String? = null

    var head: String? = null

    var background: String? = null

    var focus: FollowState = FollowState.SElF

    constructor()

    constructor(user: User) {
        BeanUtils.copyProperties(user, this)
    }
}