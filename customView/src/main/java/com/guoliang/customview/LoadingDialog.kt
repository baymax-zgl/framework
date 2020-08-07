package com.guoliang.customview

import android.app.Activity
import android.app.Dialog
import android.content.res.Resources
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.TextView

/**
 * @Description:
 * @Author: zhangguoliang
 * @CreateTime: 2020/7/3 10:49
 */
class LoadingDialog(val activity: Activity,val title:String?=null) : Dialog(activity,R.style.loading_dialog) {
    private val sResources = Resources.getSystem()
    private val width=TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 150f, sResources.displayMetrics).toInt()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflate = View.inflate(activity, R.layout.dialog_loading_layout, null)
        val txEmpty = inflate.findViewById<TextView>(R.id.tx_empty)
        title?.let { txEmpty.text=it }
        setContentView(inflate)
        setCancelable(false)
        window!!.setLayout(width,width)
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