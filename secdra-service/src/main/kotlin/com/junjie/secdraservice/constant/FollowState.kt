package com.junjie.secdraservice.constant


enum class FollowState private constructor(var value: String) {
    CONCERNED("已关注"),
    STRANGE("未关注"),
    SElF("自己")
}