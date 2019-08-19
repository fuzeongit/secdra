package com.junjie.secdraweb.vo

import com.junjie.secdraservice.constant.CollectState
import com.junjie.secdraservice.constant.PrivacyState
import com.junjie.secdraservice.document.DrawDocument
import com.junjie.secdraservice.model.Draw
import org.springframework.beans.BeanUtils
import java.util.*

class DrawVO {
    lateinit var id: String

    lateinit var introduction: String

    lateinit var url: String

    lateinit var userId: String

    lateinit var name: String

    var privacy: PrivacyState = PrivacyState.PUBLIC

    var focus: CollectState = CollectState.STRANGE

    var viewAmount: Long = 0

    var likeAmount: Long = 0

    var width: Long = 0;

    var height: Long = 0;

    var tagList: List<String> = mutableListOf()

    lateinit var user: UserVO

    lateinit var createDate: Date

    constructor()

    constructor(draw: DrawDocument) {
        BeanUtils.copyProperties(draw, this)
    }

    constructor(draw: DrawDocument, focus: CollectState, user: UserVO) {
        BeanUtils.copyProperties(draw, this)
        this.focus = focus
        this.user = user
    }

    constructor(draw: Draw) {
        this.id = draw.id!!
        this.introduction = draw.introduction
        this.url = draw.url
        this.userId = draw.userId
        this.name = draw.name
        this.privacy = draw.privacy
        this.width = draw.width
        this.height = draw.height
        this.createDate = draw.createDate!!
        this.tagList = draw.tagList.map { it.name }
    }

    constructor(draw: Draw, viewAmount: Long, likeAmount: Long) {
        this.id = draw.id!!
        this.introduction = draw.introduction
        this.url = draw.url
        this.userId = draw.userId
        this.name = draw.name
        this.privacy = draw.privacy
        this.viewAmount = viewAmount
        this.likeAmount = likeAmount
        this.width = draw.width
        this.height = draw.height
        this.createDate = draw.createDate!!
        this.tagList = draw.tagList.map { it.name }
    }
}