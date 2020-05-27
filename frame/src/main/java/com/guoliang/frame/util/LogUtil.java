package com.guoliang.frame.util;

import android.util.Log;

/**
 * @Description: log工具类
 * @Author: zhangguoliang
 * @CreateTime: 2020/5/26 18:05
 */
public class LogUtil {
    private LogUtil() {
    }
    /**
     * LogUtil实例
     */
    private static LogUtil INSTANCE;

    public static LogUtil getInstance(){
        if (INSTANCE==null){
            synchronized (LogUtil.class){
                INSTANCE = new LogUtil();
            }
        }
        return INSTANCE;
    }


    private static boolean IS_DEBUG= true;
    public void v(String TAG, String msg) {
        if (IS_DEBUG) {
            Log.v(TAG, msg);
        }
    }

    public void d(String TAG, String msg) {
        if (IS_DEBUG) {
            Log.d(TAG, msg);
        }
    }

    public void i(String TAG, String msg) {
        if (IS_DEBUG) {
            Log.i(TAG, msg);
        }
    }

    public void w(String TAG, String msg) {
        if (IS_DEBUG) {
            Log.w(TAG, msg);
        }
    }

    public void e(String TAG, String msg) {
        if (IS_DEBUG) {
            Log.e(TAG, msg);
        }
    }

    public void setIsDebug(boolean isDebug) {
        IS_DEBUG = isDebug;
    }
}
