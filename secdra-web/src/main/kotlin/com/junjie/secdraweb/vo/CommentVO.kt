package com.junjie.secdraweb.vo

import com.junjie.secdradata.database.primary.entity.Comment
import org.springframework.beans.BeanUtils
import java.util.*

class CommentVO {
    lateinit var id: String
    //图片作者id
    lateinit var authorId: String
    //评论人id
    lateinit var createdBy: String

    lateinit var pictureId: String

    lateinit var content: String

    lateinit var createDate: Date

    lateinit var author: UserVO

    lateinit var critic: UserVO

    constructor()

    constructor(comment: Comment) {
        BeanUtils.copyProperties(comment, this)
    }

    constructor(comment: Comment, author: UserVO, critic: UserVO) {
        BeanUtils.copyProperties(comment, this)
        this.author = author
        this.critic = critic
    }
}