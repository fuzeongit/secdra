package com.junjie.secdraweb.vo

import com.junjie.secdraservice.model.Reply
import org.springframework.beans.BeanUtils
import java.util.*

class ReplyVo {
    var id: String? = null
    //评论id
    var commentId: String? = null
    //图片作者id
    var authorId: String? = null
    //评论人id
    var criticId: String? = null
    //回答者id
    var answererId: String? = null
    //图片id
    var drawId: String? = null

    var content: String? = null

    var isRead: Boolean = false

    var createDate: Date = Date()

    var modifiedDate: Date = Date()

    var critic :UserVo? = null

    var answerer :UserVo? = null

    constructor() {}

    constructor(comment: Reply) {
        BeanUtils.copyProperties(comment, this)
    }
}