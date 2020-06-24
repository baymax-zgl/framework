package com.guoliang.framekt.base

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

/**
 * @Description:
 * @Author: zhangguoliang
 * @CreateTime: 2020/5/26 17:03
 */
abstract class BaseDBActivity<T : ViewDataBinding> : AppCompatActivity() {
    //布局id
    abstract val layoutId: Int

    //页面名称
    abstract val pageName: String

    //初始化
    abstract fun initView(savedInstanceState: Bundle?)

    lateinit var dataBinding:T

    //是否展示状态栏
    var showStatusBar = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = DataBindingUtil.setContentView<T>(this, layoutId)
        if (!showStatusBar) {
            // 5.0以上系统状态栏透明
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                //白色SYSTEM_UI_FLAG_LAYOUT_STABLE、深色SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                window.decorView.systemUiVisibility =
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                window.statusBarColor = Color.TRANSPARENT
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            }
        }
        ActivityManager.instance?.addActivity(this)
        initView(savedInstanceState)
    }

    fun toast(test: String?) {
        Toast.makeText(this, test, Toast.LENGTH_SHORT).show()
    }

    override fun finish() {
        super.finish()
        ActivityManager.instance?.removeActivity(this)
    }
}