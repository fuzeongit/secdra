package com.junjie.secdraweb.vo

import com.junjie.secdraservice.model.CommentMessage
import org.springframework.beans.BeanUtils
import java.util.*

class CommentMessageVO {
    lateinit var id: String
    //评论id
    lateinit var commentId: String
    //图片作者id
    lateinit var authorId: String
    //图片id
    lateinit var drawId: String
    //评论人id
    lateinit var criticId: String

    lateinit var content: String

    lateinit var critic: UserVO

    lateinit var createDate: Date

    constructor()

    constructor(commentMessage: CommentMessage) {
        BeanUtils.copyProperties(commentMessage, this)
    }

    constructor(commentMessage: CommentMessage, critic: UserVO) {
        BeanUtils.copyProperties(commentMessage, this)
        this.critic = critic
    }
}