package com.guoliang.frame.base;

import android.app.Activity;
import android.os.Process;

import java.lang.ref.WeakReference;
import java.util.ListIterator;
import java.util.Stack;

/**
 * @Description:
 * @Author: zhangguoliang
 * @CreateTime: 2020/5/26 17:14
 */
public class ActivityManager {

    private ActivityManager() {
    }

    /**
     * ActivityManager实例
     */
    private static ActivityManager INSTANCE;

    public static ActivityManager getInstance(){
        if (INSTANCE==null){
            synchronized (ActivityManager.class){
                INSTANCE = new ActivityManager();
            }
        }
        return INSTANCE;
    }


    Stack<WeakReference<Activity>> stack=null;

    /**
     * 获取栈顶Activity（堆栈中最后一个压入的）
     *
     * @return Activity
     */
    public Activity getTopActivity(){
        if (stack!=null){
            return stack.lastElement().get();
        }else {
            return null;
        }
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity){
        if (stack==null){
            stack=new Stack<>();
        }
        stack.add(new WeakReference<>(activity));
    }

    /**
     * 删除Activity
     *
     * @param activity 指定的Activity
     */
    public void removeActivity(Activity activity){
        if (stack!=null){
            stack.remove(new WeakReference<>(activity));
        }
    }

    /**
     * 通过class 获取栈顶Activity
     *
     * @param aClass Activity的class
     * @return Activity
     */
    public Activity getActivityByClass(Class aClass){
        for (WeakReference<Activity> activityWeakReference : stack) {
            if (activityWeakReference.get().getClass() == aClass){
                return activityWeakReference.get();
            }
        }
        return null;
    }

    /**
     * 结束栈顶Activity（堆栈中最后一个压入的）
     */
    public void killTopActivity(){
        if (stack!=null){
            killActivity(stack.lastElement().get());
        }
    }

    /**
     * 结束指定的Activity
     *
     * @param activity Activity
     */
    public void killActivity(Activity activity){
        if (stack!=null) {
            for (WeakReference<Activity> reference : stack) {
                if (reference.get() == activity) {
                    stack.remove(reference);
                    if (activity!=null&&!reference.get().isFinishing()) {
                        reference.get().finish();
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
    public void killActivity(Class aClass){
        if (stack!=null){
            for (WeakReference<Activity> reference : stack) {
                if (reference.get().getClass() == aClass) {
                    stack.remove(reference);
                    if (!reference.get().isFinishing()) {
                        reference.get().finish();
                    }
                }
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public void killAllActivity(){
        if (stack!=null){
            ListIterator<WeakReference<Activity>> listIterator = stack.listIterator();
            while (listIterator.hasNext()){
                Activity activity = listIterator.next().get();
                if (activity!=null&&!activity.isFinishing()){
                    activity.finish();
                    listIterator.remove();
                }
            }
        }
    }

    /**
     * 移除除了某个activity的其他所有activity
     *
     * @param aClass Class
     */
    public void killAllActivityExceptOne(Class aClass){
        if (stack!=null) {
            ListIterator<WeakReference<Activity>> listIterator = stack.listIterator();
            while (listIterator.hasNext()){
                if (listIterator.next().get().getClass()==aClass){
                    continue;
                }else {
                    killActivity(listIterator.next().get());
                    listIterator.remove();
                }
            }
        }
    }

    /**
     * 退出应用程序
     */
    public void appExit(){
        killAllActivity();
        Process.killProcess(Process.myPid());
    }

    /**
     * 栈中Activity的数
     *
     * @return Activity的数
     */
    public int getStackActivitySize(){
        if (stack!=null){
            return stack.size();
        }else {
            return 0;
        }
    }


}
