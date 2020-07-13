package com.guoliang.customview

import android.app.Activity
import android.app.Dialog

/**
 * @Description:
 * @Author: zhangguoliang
 * @CreateTime: 2020/7/3 10:49
 */
class LoadingDialog(activity: Activity) : Dialog(activity,R.style.loading_dialog) {
    init {
        setContentView(R.layout.dialog_loading_layout)
        setCancelable(false)
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

    fun setIsShow(it: Boolean) {
        if (it){
            show()
        }else{
            dismiss()
        }
    }
}