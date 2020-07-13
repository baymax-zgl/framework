package com.guoliang.framekt.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * @Description:
 * @Author: zhangguoliang
 * @CreateTime: 2020/6/24 17:26
 */
open class BaseViewModel : ViewModel() {
    val loading: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    fun launch(block: suspend CoroutineScope.() -> Unit) = viewModelScope.launch { block() }

    fun launchDialog(block: suspend CoroutineScope.() -> Unit) = viewModelScope.launch {
        loading.value = true
        block()
        loading.value = false
    }

    fun launchIO(block: suspend CoroutineScope.() -> Unit) = viewModelScope.launch(Dispatchers.IO) { block() }
}