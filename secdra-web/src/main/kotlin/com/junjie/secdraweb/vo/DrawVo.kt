package com.junjie.secdraweb.vo

import com.junjie.secdraservice.model.Tag
import java.util.*

class DrawVo() {
    var id: String? = null

    var introduction: String? = null

    var url: String? = null

    var userId: String? = null

    var name: String? = null

    var isPrivate: Boolean = false

    var isFocus: Boolean = false

    var viewAmount: Int = 0

    var likeAmount: Int = 0

    var width: Int = 0;

    var height: Int = 0;

    var tagList: MutableSet<Tag>? = null

    var user: UserVo? = null

    var createDate: Date = Date()

    var modifiedDate: Date = Date()
}