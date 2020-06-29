package com.guoliang.framekt.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment

/**
 * @Description:
 * @Author: zhangguoliang
 * @CreateTime: 2020/5/27 11:17
 */
abstract class BaseFragment : Fragment() {
    //布局id
    abstract val layoutId: Int

    //页面名称
    abstract val pageName: String?

    //初始化0
    abstract fun initView(
        fragmentView: View?,
        savedInstanceState: Bundle?
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(layoutId, container, false)
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        initView(view, savedInstanceState)
    }

    fun toast(test: String?) {
        Toast.makeText(context, test, Toast.LENGTH_SHORT).show()
    }
}