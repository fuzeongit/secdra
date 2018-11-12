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

    var viewAmount: Long = 0

    var likeAmount: Long = 0

    var width: Long = 0;

    var height: Long = 0;

    var tagList: MutableSet<Tag>? = null

    var user: UserVo? = null

    var createDate: Date = Date()

    var modifiedDate: Date = Date()
}