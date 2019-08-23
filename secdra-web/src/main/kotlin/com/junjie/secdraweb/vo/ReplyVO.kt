package com.junjie.secdraweb.vo

import com.junjie.secdraservice.model.Reply
import org.springframework.beans.BeanUtils
import java.util.*

class ReplyVO {
    lateinit var id: String
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

    lateinit var createDate: Date

    lateinit var critic: UserVO

    lateinit var answerer: UserVO

    constructor()

    constructor(comment: Reply) {
        BeanUtils.copyProperties(comment, this)
    }

    constructor(comment: Reply, critic: UserVO, answerer: UserVO) {
        BeanUtils.copyProperties(comment, this)
        this.critic = critic
        this.answerer = answerer
    }
}