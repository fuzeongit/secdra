package com.junjie.secdraweb.vo

import com.junjie.secdracore.constant.CollectState
import com.junjie.secdracore.constant.PrivacyState
import com.junjie.secdraservice.model.Draw
import org.springframework.beans.BeanUtils
import java.util.*

class DrawVO {
    var id: String? = null

    var introduction: String? = null

    var url: String? = null

    var userId: String? = null

    var name: String? = null

    var privacy: PrivacyState = PrivacyState.PUBLIC

    var focus: CollectState = CollectState.STRANGE

    var viewAmount: Long = 0

    var likeAmount: Long = 0

    var width: Long = 0;

    var height: Long = 0;

    var tagList: List<String> = mutableListOf()

    var user: UserVO? = null

    var createDate: Date = Date()

    var modifiedDate: Date = Date()

    constructor() {
    }

    constructor(draw: Draw) {
        BeanUtils.copyProperties(draw, this)
    }
}