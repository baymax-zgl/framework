package com.example.framework;

import android.view.View;

import com.guoliang.frame.util.LogUtil;

/**
 * @Description:
 * @Author: zhangguoliang
 * @CreateTime: 2020/6/3 17:25
        */
public class Presenter {
    private static final String TAG = "Presenter";
    public void onCompletedChanged(View view, User user, boolean completed){
        LogUtil.getInstance().e(TAG,""+completed);
    }
}
