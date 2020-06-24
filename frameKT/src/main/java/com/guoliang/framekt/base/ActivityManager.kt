package com.guoliang.framekt.base

import android.app.Activity
import android.os.Process
import java.lang.ref.WeakReference
import java.util.*

/**
 * @Description:
 * @Author: zhangguoliang
 * @CreateTime: 2020/5/26 17:14
 */
class ActivityManager private constructor() {
    var stack: Stack<WeakReference<Activity>>? = null

    /**
     * 获取栈顶Activity（堆栈中最后一个压入的）
     *
     * @return Activity
     */
    val topActivity: Activity?
        get() = if (stack != null) {
            stack!!.lastElement().get()
        } else {
            null
        }

    /**
     * 添加Activity到堆栈
     */
    fun addActivity(activity: Activity) {
        if (stack == null) {
            stack = Stack()
        }
        stack!!.add(WeakReference(activity))
    }

    /**
     * 删除Activity
     *
     * @param activity 指定的Activity
     */
    fun removeActivity(activity: Activity) {
        if (stack != null) {
            stack!!.remove(WeakReference(activity))
        }
    }

    /**
     * 通过class 获取栈顶Activity
     *
     * @param aClass Activity的class
     * @return Activity
     */
    fun getActivityByClass(aClass: Class<*>): Activity? {
        for (activityWeakReference in stack!!) {
            if (activityWeakReference.get()!!.javaClass == aClass) {
                return activityWeakReference.get()
            }
        }
        return null
    }

    /**
     * 结束栈顶Activity（堆栈中最后一个压入的）
     */
    fun killTopActivity() {
        if (stack != null) {
            killActivity(stack!!.lastElement().get())
        }
    }

    /**
     * 结束指定的Activity
     *
     * @param activity Activity
     */
    fun killActivity(activity: Activity?) {
        if (stack != null) {
            for (reference in stack!!) {
                if (reference.get() === activity) {
                    stack!!.remove(reference)
                    if (activity != null && !reference.get()!!.isFinishing) {
                        reference.get()!!.finish()
                    }
                }
            }
        }
    }

    /**
     * 结束指定类名的Activity
     *
     * @param aClass Class
     */
    fun killActivity(aClass: Class<*>) {
        if (stack != null) {
            for (reference in stack!!) {
                if (reference.get()!!.javaClass == aClass) {
                    stack!!.remove(reference)
                    if (!reference.get()!!.isFinishing) {
                        reference.get()!!.finish()
                    }
                }
            }
        }
    }

    /**
     * 结束所有Activity
     */
    fun killAllActivity() {
        if (stack != null) {
            val listIterator =
                stack!!.listIterator()
            while (listIterator.hasNext()) {
                val activity = listIterator.next().get()
                if (activity != null && !activity.isFinishing) {
                    activity.finish()
                    listIterator.remove()
                }
            }
        }
    }

    /**
     * 移除除了某个activity的其他所有activity
     *
     * @param aClass Class
     */
    fun killAllActivityExceptOne(aClass: Class<*>) {
        if (stack != null) {
            val listIterator =
                stack!!.listIterator()
            while (listIterator.hasNext()) {
                if (listIterator.next().get()!!.javaClass == aClass) {
                    continue
                } else {
                    killActivity(listIterator.next().get())
                    listIterator.remove()
                }
            }
        }
    }

    /**
     * 退出应用程序
     */
    fun appExit() {
        killAllActivity()
        Process.killProcess(Process.myPid())
    }

    /**
     * 栈中Activity的数
     *
     * @return Activity的数
     */
    val stackActivitySize: Int
        get() = if (stack != null) {
            stack!!.size
        } else {
            0
        }

    companion object {
        /**
         * ActivityManager实例
         */
        private var INSTANCE: ActivityManager? = null
        val instance: ActivityManager?
            get() {
                if (INSTANCE == null) {
                    synchronized(
                        ActivityManager::class.java
                    ) {
                        INSTANCE =
                            ActivityManager()
                    }
                }
                return INSTANCE
            }
    }
}