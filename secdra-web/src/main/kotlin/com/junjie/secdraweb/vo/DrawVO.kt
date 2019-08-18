package com.junjie.secdraweb.vo

import com.junjie.secdraservice.constant.CollectState
import com.junjie.secdraservice.constant.PrivacyState
import com.junjie.secdraservice.document.DrawDocument
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

    var viewAmount: Long? = null

    var likeAmount: Long? = null

    var width: Long = 0;

    var height: Long = 0;

    var tagList: List<String> = mutableListOf()

    var user: UserVO? = null

    var createDate: Date = Date()

    var modifiedDate: Date = Date()

    constructor() {
    }

    constructor(draw: DrawDocument) {
        BeanUtils.copyProperties(draw, this)
    }

    constructor(draw: Draw) {
        this.id = draw.id
        this.introduction = draw.introduction
        this.url = draw.url
        this.userId = draw.userId
        this.name = draw.name
        this.privacy = draw.privacy
        this.width = draw.width
        this.height = draw.height
        this.createDate = draw.createDate
        this.modifiedDate = draw.modifiedDate
        this.tagList = draw.tagList.map { it.name }
    }

    constructor(draw: Draw,viewAmount:Long,likeAmount:Long) {
        this.id = draw.id
        this.introduction = draw.introduction
        this.url = draw.url
        this.userId = draw.userId
        this.name = draw.name
        this.privacy = draw.privacy
        this.viewAmount = viewAmount
        this.likeAmount = likeAmount
        this.width = draw.width
        this.height = draw.height
        this.createDate = draw.createDate
        this.modifiedDate = draw.modifiedDate
        this.tagList = draw.tagList.map { it.name }
    }
}