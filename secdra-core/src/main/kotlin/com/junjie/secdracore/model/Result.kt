package com.junjie.secdracore.model

/**
 * 返回信息
 * @author fjj
 * @param <T>
</T> */
class Result<T> {
    var status: Int? = null

    var message: String? = null

    var data: T? = null

    constructor(data: T) {
        this.status = 200
        this.data = data
    }

    constructor(status: Int?, message: String?) {
        this.status = status
        this.message = message
    }

    constructor(status: Int?, message: String?, data: T) {
        this.status = status
        this.message = message
        this.data = data
    }
}