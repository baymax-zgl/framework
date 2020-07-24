package com.guoliang.framekt.network

import java.lang.Exception

/**
 * @Description:
 * @Author: zhangguoliang
 * @CreateTime: 2020/7/24 10:04
 */
open class KTBaseRemoteRepository {
    suspend fun <T :Any> safeApiCall(call: suspend () -> T):ResultKT<T> = try {
        ResultKT.Success(call.invoke())
    }catch (e :Exception){
        ResultKT.Error(ExceptionHandle.handleException(e))
    }

    suspend fun <T :BaseApiEntity> safeApiCallFilterResults(call: suspend () -> T):ResultKT<T> = try {
        val data = call.invoke()
        if (data.code==10000){
            ResultKT.Success(call.invoke())
        }else{
            ResultKT.Error(Exception(data.message))
        }
    }catch (e :Exception){
        ResultKT.Error(ExceptionHandle.handleException(e))
    }
}