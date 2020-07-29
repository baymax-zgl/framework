package com.guoliang.framekt.util

import android.content.res.Resources
import android.util.TypedValue

/**
 * @Description:
 * @Author: zhangguoliang
 * @CreateTime: 2020/5/26 18:08
 */
object DensityUtil {
    private val sResources = Resources.getSystem()
    fun dip2px(dp: Float): Float {
        val dm = sResources.displayMetrics
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, dm)
    }

    fun dip2pxInt(dp: Float): Int {
        return dip2px(dp).toInt()
    }

    fun sp2px(sp: Float): Float {
        val dm = sResources.displayMetrics
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, sp, dm)
    }

    fun sp2pxInt(sp: Float): Int {
        return sp2px(sp).toInt()
    }

    /**
     * 根据手机的分辨率PX(像素)转成DP
     * @param pxValue
     * @return
     */
    fun px2dip(pxValue: Float): Int {
        val scale = sResources.displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     * @param pxValue
     * @return
     */
    fun px2sp(pxValue: Float): Int {
        val fontScale = sResources.displayMetrics.scaledDensity
        return (pxValue / fontScale + 0.5f).toInt()
    }
}