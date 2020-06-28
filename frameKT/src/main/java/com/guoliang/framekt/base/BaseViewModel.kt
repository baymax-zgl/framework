package com.guoliang.framekt.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * @Description:
 * @Author: zhangguoliang
 * @CreateTime: 2020/6/24 17:26
 */
open class BaseViewModel : ViewModel() {
    fun launch(block:suspend CoroutineScope.() -> Unit)=viewModelScope.launch { block() }
}