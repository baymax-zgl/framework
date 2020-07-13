package com.guoliang.framekt.base

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

/**
 * @Description:
 * @Author: zhangguoliang
 * @CreateTime: 2020/7/6 15:48
 */
abstract class BaseDBDialog<DB : ViewDataBinding> : Dialog {
    constructor(context: Context) : super(context) {}
    constructor(context: Context, themeResId: Int) : super(context, themeResId) {}

    lateinit var mBinding: DB
    abstract val layoutId: Int
    abstract fun initView(savedInstanceState: Bundle?)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding=DataBindingUtil.inflate(LayoutInflater.from(context),layoutId,null,false)
        setContentView(mBinding.root)
        initView(savedInstanceState)
    }

    override fun show() {
        try {
            if (!isShowing) {
                super.show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun dismiss() {
        try {
            if (isShowing) {
                super.dismiss()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
