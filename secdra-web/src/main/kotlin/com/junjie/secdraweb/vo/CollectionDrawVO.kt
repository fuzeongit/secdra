package com.junjie.secdraweb.vo

import com.junjie.secdraservice.constant.CollectDrawState
import com.junjie.secdraservice.constant.CollectState
import com.junjie.secdraservice.constant.PrivacyState
import com.junjie.secdraservice.document.DrawDocument
import org.springframework.beans.BeanUtils
import java.util.*

class CollectionDrawVO {
    lateinit var id: String

    var url: String? = null

    var userId: String? = null

    var state: CollectDrawState = CollectDrawState.NORMAL

    var privacy: PrivacyState = PrivacyState.PUBLIC

    var focus: CollectState = CollectState.STRANGE

    var width: Long = 0;

    var height: Long = 0;

    var user: UserVO? = null

    var createDate: Date = Date()

    var modifiedDate: Date = Date()

    constructor()

    constructor(id: String) {
        this.id = id
        //TODO 设置默认图片
        this.url = ""
        this.state = CollectDrawState.EMPTY
    }

    constructor(draw: DrawDocument, focus: CollectState, user: UserVO) {
        BeanUtils.copyProperties(draw, this)
        this.focus = focus
        this.user = user
    }
}