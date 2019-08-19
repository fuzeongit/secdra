package com.junjie.secdraweb.vo

import com.junjie.secdraservice.model.CommentMessage
import com.junjie.secdraservice.model.FollowMessage
import org.springframework.beans.BeanUtils
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.util.*

class FollowMessageVO {
    lateinit var id: String

    lateinit var followerId: String

    lateinit var followingId: String

    var isRead: Boolean = false

    lateinit var createDate: Date

    lateinit var follower: UserVO

    constructor()

    constructor(followMessage: FollowMessage) {
        BeanUtils.copyProperties(followMessage, this)
    }
}