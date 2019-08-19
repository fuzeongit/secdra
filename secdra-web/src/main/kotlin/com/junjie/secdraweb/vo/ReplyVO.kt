package com.junjie.secdraweb.vo

import com.junjie.secdraservice.model.Reply
import org.springframework.beans.BeanUtils
import java.util.*

class ReplyVO {
    var id: String? = null
    //评论id
    lateinit var commentId: String
    //图片作者id
    lateinit var authorId: String
    //评论人id
    lateinit var criticId: String
    //回答者id
    lateinit var answererId: String
    //图片id
    lateinit var drawId: String

    lateinit var content: String

    var createDate: Date = Date()

    var modifiedDate: Date = Date()

    lateinit var critic: UserVO

    lateinit var answerer: UserVO

    constructor()

    constructor(comment: Reply) {
        BeanUtils.copyProperties(comment, this)
    }
}