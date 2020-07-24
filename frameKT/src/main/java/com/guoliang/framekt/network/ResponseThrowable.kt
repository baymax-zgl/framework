package com.guoliang.framekt.network

import com.guoliang.framekt.network.ERROR

/**
 * @Description:
 * @Author: zhangguoliang
 * @CreateTime: 2020/7/24 9:54
 */
class ResponseThrowable : Exception {
    var code: Int
    var errMsg: String

    constructor(error: ERROR, e: Throwable? = null) : super(e) {
        code = error.getKey()
        errMsg = error.getValue()
    }

    constructor(code: Int, msg: String, e: Throwable? = null) : super(e) {
        this.code = code
        this.errMsg = msg
    }
}

