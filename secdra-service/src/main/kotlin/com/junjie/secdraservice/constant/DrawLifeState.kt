package com.junjie.secdraservice.constant

enum class DrawLifeState private constructor(var value: String) {
    DISAPPEAR("不存在"),
    EXIST("正常")
}