package com.junjie.secdraservice.contant

enum class NotifyType private constructor(var value: String) {
    COMMENT("评论"),
    REPLY("回复"),
    BROADCAST("广播"),
}
