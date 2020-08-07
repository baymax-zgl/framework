package com.guoliang.framekt.util.permission_observable


/**
 * Author:     zhangguoliang
 * CreateDate: 2019/5/14 11:50
 */
class PermissionPostman : PermissionObservable {
    //快递员
    companion object {
        val SUCCESS: Int = 0
        val FAILURE: Int = 0
    }

    private val personList = ArrayList<PermissionObserver>()//保存收件人（观察者）的信息
    override fun add(permissionObserver: PermissionObserver) {//添加收件人
        personList.add(permissionObserver)
    }

    override fun remove(permissionObserver: PermissionObserver) {//移除收件人
        personList.remove(permissionObserver)
    }

    override fun notify(message: Int) {//逐一通知收件人（观察者）
        for (observer in personList) {
            if (message == SUCCESS) {
                observer.authorizationSuccess()
            } else {
                observer.authorizationFailure()
            }
        }
    }
    override fun notifyFirstPermissionMap(map:Map<String,Int>){
        for (observer in personList) {
            observer.firstAuthorization(map)
        }
    }
}