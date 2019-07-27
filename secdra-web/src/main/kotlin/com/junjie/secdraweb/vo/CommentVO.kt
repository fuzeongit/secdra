package com.junjie.secdraweb.vo

import com.junjie.secdraservice.model.Comment
import org.springframework.beans.BeanUtils
import java.util.*

class CommentVO {
    var id: String? = null
    //图片作者id
    var authorId: String? = null
    //评论人id
    var criticId: String? = null

    var drawId: String? = null

    var content: String? = null

    var createDate: Date = Date()

    var modifiedDate: Date = Date()

    var author: UserVO? = null
    
    var critic: UserVO? = null

    constructor() {}

    constructor(comment: Comment) {
        BeanUtils.copyProperties(comment, this)
    }
}