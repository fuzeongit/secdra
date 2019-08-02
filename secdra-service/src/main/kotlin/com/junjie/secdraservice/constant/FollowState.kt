package com.junjie.secdraservice.constant


enum class FollowState private constructor(var value: String) {
    STRANGE("未关注"),
    CONCERNED("已关注"),
    SElF("自己")
}