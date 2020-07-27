package com.guoliang.framekt.network

import com.guoliang.framekt.R

/**
 * @Description:
 * @Author: zhangguoliang
 * @CreateTime: 2020/7/24 9:54
 */
enum class ERROR(private val code: Int, private val errorMessageId: Int) {

    /**
     * 未知错误
     */
    UNKNOWN(1000, R.string.unknown_error),
    /**
     * 解析错误
     */
    PARSE_ERROR(1001, R.string.parse_error),
    /**
     * 网络错误
     */
    NETWORD_ERROR(1002, R.string.please_check_the_network_status),
    /**
     * 协议出错
     */
    HTTP_ERROR(1003, R.string.protocol_error),

    /**
     * 证书出错
     */
    SSL_ERROR(1004, R.string.certificate_error),

    /**
     * 连接超时
     */
    TIMEOUT_ERROR(1006, R.string.connection_timeout);

    fun getValue(): Int {
        return errorMessageId
    }

    fun getKey(): Int {
        return code
    }

}