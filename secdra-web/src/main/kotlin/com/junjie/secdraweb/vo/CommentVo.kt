package com.junjie.secdraweb.vo

import com.junjie.secdraservice.model.Comment
import org.springframework.beans.BeanUtils
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.util.*

class CommentVo {
    var id: String? = null
    //图片作者id
    var authorId: String? = null
    //评论人id
    var criticId: String? = null

    var drawId: String? = null

    var content: String? = null

    var isRead: Boolean = false
//    //点赞数
//    var like: Number = 0

    @CreatedDate
    var createDate: Date = Date()

    @LastModifiedDate
    var modifiedDate: Date = Date()

    var author: UserVo? = null
    
    var critic: UserVo? = null

    constructor() {}

    constructor(comment: Comment) {
        BeanUtils.copyProperties(comment, this)
    }
}