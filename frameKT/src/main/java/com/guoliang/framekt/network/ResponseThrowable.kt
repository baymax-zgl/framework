package com.guoliang.framekt.network

/**
 * @Description:
 * @Author: zhangguoliang
 * @CreateTime: 2020/7/24 9:54
 */
class ResponseThrowable : Exception {
    var code: Int
    var errMsgID: Int

    constructor(error: ERROR, e: Throwable? = null) : super(e) {
        code = error.getKey()
        errMsgID = error.getValue()
    }

    constructor(code: Int, msgID: Int, e: Throwable? = null) : super(e) {
        this.code = code
        this.errMsgID = msgID
    }
}

