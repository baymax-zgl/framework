package com.guoliang.frame.util;

import android.content.Context;
import android.os.Build;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Author:     zhangguoliang
 * CreateDate: 2019/8/15 10:39
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {

    private static final String TAG = "CrashHandler";
    private String filePath = "";
    /**
     * 系统默认的UncaughtException处理类
     */
    private Thread.UncaughtExceptionHandler mDefaultException;
    /**
     * 程序的Context对象
     */
    private Context mContext;
    /**
     * 错误报告文件的扩展名
     */
    private static final String CRASH_REPORTER_EXTENSION = ".txt";

    /**
     * CrashHandler实例
     */
    private static CrashHandler INSTANCE;

    /**
     * 保证只有一个CrashHandler实例
     */
    private CrashHandler() {
    }

    /**
     * 获取CrashHandler实例 ,单例模式
     */
    public static CrashHandler getInstance() {
        if (INSTANCE == null) {
            synchronized (CrashHandler.class) {
                if (INSTANCE == null) {
                    INSTANCE = new CrashHandler();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * 初始化,注册Context对象,
     * 获取系统默认的UncaughtException处理器,
     * 设置该CrashHandler为程序的默认处理器
     *
     * @param ctx
     */
    public void init(Context ctx,String path) {
        mContext = ctx;
        mDefaultException = Thread.getDefaultUncaughtExceptionHandler();
        filePath= path+"Crash/";
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * 当UncaughtException发生时会转入该函数来处理
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handlerException(ex) && mDefaultException != null) {
            // 如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultException.uncaughtException(thread, ex);
        } else { //否则自己进行处理
            try {  //Sleep 来让线程停止一会是为了显示Toast信息给用户，然后Kill程序
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Log.e("CrashHandler", "uncaughtException: "+e.getMessage());
            }catch (Exception e){
                Log.e("CrashHandler", "Exception: "+e.getMessage());
            }
            //如果不关闭程序,会导致程序无法启动,需要完全结束进程才能重新启动
            //System.exit(0)表示是正常退出；
            //System.exit(1)表示是非正常退出，通常这种退出方式应该放在catch块中。
//            android.os.Process.killProcess(android.os.Process.myPid());
//            System.exit(10);
            mDefaultException.uncaughtException(thread, ex);
        }
    }

    /**
     * 自定义错误处理,收集错误信息
     * 发送错误报告等操作均在此完成.
     * 开发者可以根据自己的情况来自定义异常处理逻辑
     */
    private boolean  handlerException(Throwable ex) {
        if (ex == null) {
            Log.w(TAG, "handleException--- ex==null");
            return false;
        }
        //收集设备信息
        //保存错误报告文件
        saveCrashInfoToFile(ex);
        //使用Toast来显示异常信息
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(mContext, "很抱歉,程序出现异常,即将退出.", Toast.LENGTH_LONG).show();
                Looper.loop();
            }
        }.start();
        return true;
    }

    /**
     * 保存错误信息到文件中
     *
     * @param ex
     * @return
     */
    private void saveCrashInfoToFile(Throwable ex) {
        Writer info = new StringWriter();
        PrintWriter printWriter = new PrintWriter(info);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        String result = info.toString();
        printWriter.close();
        StringBuilder sb = new StringBuilder();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        String now = sdf.format(new Date());
        sb.append("TIME:").append(now);//崩溃时间
        //程序信息
        sb.append("\nAPPLICATION_ID:").append(AppUtils.getPackageName(mContext));//软件APPLICATION_ID
        sb.append("\nVERSION_CODE:").append(AppUtils.getVersionCode(mContext));//软件版本号
        sb.append("\nVERSION_NAME:").append(AppUtils.getVersionName(mContext));//VERSION_NAME
        sb.append("\nBUILD_TYPE:").append(AppUtils.isApkInDebug(mContext));//是否是DEBUG版本
        //设备信息
        sb.append("\nMODEL:").append(android.os.Build.MODEL);
        sb.append("\nRELEASE:").append(Build.VERSION.RELEASE);
        sb.append("\nSDK:").append(Build.VERSION.SDK_INT);
        sb.append("\nEXCEPTION:").append(ex.getLocalizedMessage());
        sb.append("\nSTACK_TRACE:").append(result);
        try {
            File file = new File(filePath);
            if (!file.exists()){
                boolean mkdirs = file.mkdirs();
            }
            FileWriter writer = new FileWriter(filePath + now + CRASH_REPORTER_EXTENSION);
            writer.write(sb.toString());
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
