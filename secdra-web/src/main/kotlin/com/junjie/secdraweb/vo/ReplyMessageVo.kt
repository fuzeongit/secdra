package com.junjie.secdraweb.vo

import com.junjie.secdraservice.model.ReplyMessage
import org.springframework.beans.BeanUtils
import java.util.*

class ReplyMessageVo {
    var id: String? = null
    //评论id
    var commentId: String? = null
    //回复id
    var replyId: String? = null
    //图片作者id
    var authorId: String? = null
    //图片id
    var drawId: String? = null
    //评论人id
    var criticId: String? = null
    //回答者id
    var answererId: String? = null

    var content: String? = null

    var isRead: Boolean = false

    var createDate: Date = Date()

    var modifiedDate: Date = Date()

    var answerer:UserVo? = null

    constructor() {}

    constructor(replyMessage: ReplyMessage) {
        BeanUtils.copyProperties(replyMessage, this)
    }
}