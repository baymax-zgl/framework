package com.guoliang.frame.base;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * @Description:
 * @Author: zhangguoliang
 * @CreateTime: 2020/5/26 17:03
 */
public abstract class BaseActivity extends AppCompatActivity {
    //布局id
    public abstract int getLayoutId();
    //页面名称
    public abstract String getPageName();
    //初始化
    public abstract void initView(Bundle savedInstanceState);

    //是否展示状态栏
    public boolean showStatusBar=false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getLayoutId()!=0){
            setContentView(getLayoutId());
        }
        if (!showStatusBar){
            Window window = getWindow();
            // 5.0以上系统状态栏透明
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                //白色SYSTEM_UI_FLAG_LAYOUT_STABLE、深色SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.TRANSPARENT);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
        }
        ActivityManager.getInstance().addActivity(this);
        initView(savedInstanceState);
    }

    public void toast(String test){
        Toast.makeText(this, test, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void finish() {
        super.finish();
        ActivityManager.getInstance().removeActivity(this);
    }
}
