package com.guoliang.framekt.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel

/**
 * @Description:
 * @Author: zhangguoliang
 * @CreateTime: 2020/6/28 9:41
 */
abstract class BaseVMFragment<VM : ViewModel,DB : ViewDataBinding>(useDataBinding: Boolean = true) : Fragment() {

    private val _useBinding = useDataBinding
    protected lateinit var mBinding: DB
    protected lateinit var mViewModel: VM

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return if (_useBinding) {
            mBinding = DataBindingUtil.inflate(inflater, layoutId, container, false)
            mBinding.root
        } else
            inflater.inflate(layoutId, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (initVM!=null) {
            mViewModel = initVM!!
        }
        if (_useBinding) mBinding.lifecycleOwner = this
        initView()
        initData()
        startObserve()
        super.onViewCreated(view, savedInstanceState)
    }

    abstract val layoutId: Int
    abstract val initVM: VM?
    abstract fun initView()
    abstract fun initData()
    abstract fun startObserve()
}