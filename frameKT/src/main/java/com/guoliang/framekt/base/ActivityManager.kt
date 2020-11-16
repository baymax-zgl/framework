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
    private var stack: Stack<Activity> = Stack()

    /**
     * 获取栈顶Activity（堆栈中最后一个压入的）
     *
     * @return Activity
     */
    val topActivity: Activity
        get() = stack.lastElement()

    /**
     * 添加Activity到堆栈
     */
    fun addActivity(activity: Activity) {
        stack.add(activity)
    }

    /**
     * 删除Activity
     *
     * @param activity 指定的Activity
     */
    fun removeActivity(activity: Activity) {
        stack.remove(activity)
    }

    /**
     * 通过class 获取栈顶Activity
     *
     * @param aClass Activity的class
     * @return Activity
     */
    fun getActivityByClass(aClass: Class<*>): Activity? {
        for (activityWeakReference in stack) {
            if (activityWeakReference.javaClass == aClass) {
                return activityWeakReference
            }
        }
        return null
    }

    /**
     * 结束栈顶Activity（堆栈中最后一个压入的）
     */
    fun killTopActivity() {
        killActivity(stack.lastElement())
    }

    /**
     * 结束指定的Activity
     *
     * @param activity Activity
     */
    fun killActivity(activity: Activity?) {
        for (reference in stack) {
            if (reference === activity) {
                stack.remove(reference)
                if (activity != null && !reference!!.isFinishing) {
                    reference.finish()
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
        for (reference in stack) {
            if (reference!!.javaClass == aClass) {
                stack.remove(reference)
                if (!reference.isFinishing) {
                    reference.finish()
                }
            }
        }
    }

    /**
     * 结束所有Activity
     */
    fun killAllActivity() {
        val listIterator = stack.listIterator()
        while (listIterator.hasNext()) {
            val activity = listIterator.next()
            if (activity != null && !activity.isFinishing) {
                activity.finish()
            }
        }
        stack.removeAllElements()
    }

    /**
     * 移除除了某个activity的其他所有activity
     *
     * @param aClass Class
     */
    fun killAllActivityExceptOne(aClass: Class<*>) {
        val listIterator = stack.listIterator()
        while (listIterator.hasNext()) {
            if (listIterator.next().javaClass == aClass) {
                continue
            } else {
                killActivity(listIterator.next())
                listIterator.remove()
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
        get() = stack.size

    companion object {
        val instance = ActivityManagerHolder.holder
    }
    private object ActivityManagerHolder {
        val holder = ActivityManager()
    }
}