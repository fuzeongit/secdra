package com.junjie.secdraservice.constant

enum class MessageType private constructor(var value: String) {
    COMMENT("评论"),
    REPLY("回复"),
    FOLLOW("关注"),
    SYSTEM("系统"),
}
