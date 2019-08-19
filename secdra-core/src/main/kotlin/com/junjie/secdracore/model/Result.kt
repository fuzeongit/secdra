package com.junjie.secdracore.model

/**
 * 返回信息
 * @author fjj
 * @param <T>
</T> */
class Result<T> {
    var status: Int = 200

    var message: String? = null

    var data: T? = null

    constructor(data: T) {
        this.data = data
    }

//    constructor(status: Int?, message: String?) {
//        this.status = status
//        this.message = message
//    }

    constructor(message: String?, data: T? = null) {
        this.message = message
        this.data = data
    }

    constructor(status: Int, message: String?, data: T? = null) {
        this.status = status
        this.message = message
        this.data = data
    }
}