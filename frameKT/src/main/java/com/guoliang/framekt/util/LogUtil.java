package com.guoliang.framekt.util;

import android.util.Log;

/**
 * @Description: log工具类
 * @Author: zhangguoliang
 * @CreateTime: 2020/5/26 18:05
 */
public class LogUtil {


    private static boolean IS_DEBUG= true;
    public static void v(String TAG, String msg) {
        if (IS_DEBUG) {
            Log.v(TAG, msg);
        }
    }

    public static void d(String TAG, String msg) {
        if (IS_DEBUG) {
            Log.d(TAG, msg);
        }
    }

    public static void i(String TAG, String msg) {
        if (IS_DEBUG) {
            Log.i(TAG, msg);
        }
    }

    public static void w(String TAG, String msg) {
        if (IS_DEBUG) {
            Log.w(TAG, msg);
        }
    }

    public static void e(String TAG, String msg) {
        if (IS_DEBUG) {
            Log.e(TAG, msg);
        }
    }

    public static void setIsDebug(boolean isDebug) {
        IS_DEBUG = isDebug;
    }
}
