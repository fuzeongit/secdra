package com.junjie.secdraweb.vo

import com.junjie.secdradata.constant.FollowState
import com.junjie.secdradata.constant.Gender
import com.junjie.secdradata.database.primary.entity.User
import org.springframework.beans.BeanUtils
import java.util.*

class UserVO {
    lateinit var id: String

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