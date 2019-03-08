package com.junjie.secdraweb.vo

import com.junjie.secdraservice.constant.Gender
import com.junjie.secdraservice.model.User
import org.springframework.beans.BeanUtils
import java.util.*

class UserVo {
    var id: String? = null

    var phone: String? = null

    var name: String? = null

    var gender: Gender = Gender.MALE

    var head: String? = null

    var birthday: Date? = Date()

    var introduction: String? = null

    var address: String? = null

    var background: String? = null

    var isFocus: Boolean? = null

    constructor(){
    }
    constructor(user: User){
        BeanUtils.copyProperties(user,this)
    }
}