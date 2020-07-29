package com.guoliang.framekt.base

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.*

/**
 * @Description:
 * @Author: zhangguoliang
 * @CreateTime: 2020/7/6 15:48
 */
abstract class BaseVMDialog<VM : ViewModel,DB : ViewDataBinding> : Dialog,
    ViewModelStoreOwner {

    constructor(context: Context) : super(context) {}
    constructor(context: Context, themeResId: Int) : super(context, themeResId) {}

    lateinit var mBinding: DB
    lateinit var mViewModel: VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding=DataBindingUtil.inflate(LayoutInflater.from(context),layoutId,null,false)
        if (initVM!=null) {
            mViewModel = initVM!!
        }
        setContentView(mBinding.root)
        initView(savedInstanceState)
        startObserve()
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
                viewModelStore.clear()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    abstract val layoutId: Int
    abstract val initVM: VM?
    abstract fun initView(savedInstanceState: Bundle?)
    abstract fun startObserve()


    override fun getViewModelStore(): ViewModelStore {
        return ViewModelStore()
    }
}
