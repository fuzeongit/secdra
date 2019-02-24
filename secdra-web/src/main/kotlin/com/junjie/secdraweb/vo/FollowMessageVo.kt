package com.junjie.secdraweb.vo

import com.junjie.secdraservice.model.CommentMessage
import com.junjie.secdraservice.model.FollowMessage
import org.springframework.beans.BeanUtils
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.util.*

class FollowMessageVo {
    var id: String? = null

    var followerId: String? = null

    var followingId: String? = null

    var isRead: Boolean = false

    var createDate: Date = Date()

    var modifiedDate: Date = Date()

    var follower: UserVo? = null

    constructor() {}

    constructor(followMessage: FollowMessage) {
        BeanUtils.copyProperties(followMessage, this)
    }
}