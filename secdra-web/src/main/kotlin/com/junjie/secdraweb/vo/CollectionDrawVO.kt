package com.junjie.secdraweb.vo

import com.junjie.secdraservice.constant.DrawLifeState
import com.junjie.secdraservice.constant.CollectState
import com.junjie.secdraservice.constant.PrivacyState
import com.junjie.secdraservice.document.DrawDocument
import org.springframework.beans.BeanUtils
import java.util.*

/**
 * 收藏图片的vo
 * @author fjj
 * 这里的id是图片的id，创建时间为收藏的创建时间
 */
class CollectionDrawVO {
    lateinit var id: String

    var url: String? = null

    var userId: String? = null

    var life: DrawLifeState = DrawLifeState.EXIST

    var privacy: PrivacyState = PrivacyState.PUBLIC

    var focus: CollectState = CollectState.STRANGE

    var width: Long = 0

    var height: Long = 0

    var user: UserVO? = null

    var createDate: Date? = null

    constructor()

    constructor(id: String, focus: CollectState, createDate: Date) {
        this.id = id
        //TODO 设置默认图片
        this.url = ""
        this.focus = focus
        this.createDate = createDate
        this.life = DrawLifeState.DISAPPEAR
    }

    constructor(draw: DrawDocument, focus: CollectState, createDate: Date, user: UserVO) {
        this.id = draw.id!!
        this.url = draw.url
        this.userId = draw.userId
        this.privacy = draw.privacy
        this.width = draw.width
        this.height = draw.height
        this.createDate = createDate
        this.focus = focus
        this.user = user
    }
}