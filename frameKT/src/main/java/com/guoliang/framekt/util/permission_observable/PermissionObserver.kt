package com.guoliang.framekt.util.permission_observable

/**
 * Author:     zhangguoliang
 * CreateDate: 2019/5/14 11:49
 */
interface PermissionObserver {

    //授权成功
    fun authorizationSuccess()

    //授权失败
    fun authorizationFailure()

    fun firstAuthorization(map:Map<String,Int>)
}