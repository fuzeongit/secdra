package com.junjie.secdraweb.vo

import com.junjie.secdraservice.model.CommentMessage
import org.springframework.beans.BeanUtils
import java.util.*

class CommentMessageVO {
    var id: String? = null
    //评论id
    var commentId: String? = null
    //图片作者id
    var authorId: String? = null
    //图片id
    var drawId: String? = null
    //评论人id
    var criticId: String? = null

    var content: String? = null

    var isRead: Boolean = false

    var critic: UserVO? = null

    var createDate: Date = Date()

    var modifiedDate: Date = Date()

    constructor() {}

    constructor(commentMessage: CommentMessage) {
        BeanUtils.copyProperties(commentMessage, this)
    }
}