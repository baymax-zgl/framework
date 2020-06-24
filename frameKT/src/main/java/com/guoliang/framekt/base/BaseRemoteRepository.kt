package com.guoliang.framekt.base

/**
 * @Description:
 * @Author: BaseRemoteRepository
 * @CreateTime: 2020/6/24 17:39
 */
class BaseRemoteRepository {
    sealed class Result{
        data class Success<T>(val data: T) : Result()
        data class Error(val e: Throwable) : Result()
    }
}