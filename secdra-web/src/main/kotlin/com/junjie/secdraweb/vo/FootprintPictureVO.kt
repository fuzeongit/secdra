package com.junjie.secdraweb.vo

import com.junjie.secdradata.constant.CollectState
import com.junjie.secdradata.constant.PictureLifeState
import com.junjie.secdradata.constant.PrivacyState
import com.junjie.secdradata.index.primary.document.PictureDocument
import java.util.*

/**
 * 足迹图片的vo
 * @author fjj
 * 这里的id是图片的id，创建时间为收藏的创建时间
 */
class FootprintPictureVO {
    var id: String

    var url: String? = null

    var life: PictureLifeState = PictureLifeState.EXIST

    var privacy: PrivacyState = PrivacyState.PUBLIC

    var focus: CollectState = CollectState.STRANGE

    var width: Long = 0

    var height: Long = 0

    var user: UserVO? = null

    var lastModifiedDate: Date? = null

    constructor(id: String, focus: CollectState, lastModifiedDate: Date) {
        this.id = id
        this.url = ""
        this.focus = focus
        this.lastModifiedDate = lastModifiedDate
        this.life = PictureLifeState.DISAPPEAR
    }

    constructor(picture: PictureDocument, focus: CollectState, lastModifiedDate: Date, user: UserVO) {
        this.id = picture.id!!
        this.url = picture.url
        this.privacy = picture.privacy
        this.width = picture.width
        this.height = picture.height
        this.lastModifiedDate = lastModifiedDate
        this.focus = focus
        this.user = user
    }
}