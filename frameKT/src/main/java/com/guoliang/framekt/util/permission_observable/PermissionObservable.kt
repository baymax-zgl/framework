package com.guoliang.framekt.util.permission_observable

/**
 * Author:     zhangguoliang
 * CreateDate: 2019/5/14 11:49
 */
interface PermissionObservable {
    //抽象被观察者
    fun add(permissionObserver: PermissionObserver) //添加观察者

    fun remove(permissionObserver: PermissionObserver) //删除观察者

    fun notify(message: Int) //通知观察者

    fun notifyFirstPermissionMap(map:Map<String,Int>) //通知观察者
}