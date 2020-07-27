package com.guoliang.framekt.network

/**
 * @Description:
 * @Author: zhangguoliang
 * @CreateTime: 2020/6/28 10:02
 */
sealed class ResultKT<out T : Any> {

    data class Success<out T : Any>(val data: T) : ResultKT<T>()
    data class Error(val exception: Exception?,val message: String) : ResultKT<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
        }
    }
}