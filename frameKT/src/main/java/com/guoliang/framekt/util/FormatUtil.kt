package com.guoliang.framekt.util

import android.text.TextUtils
import java.text.SimpleDateFormat
import java.util.*


/**
 * @Description:
 * @Author: zhangguoliang
 * @CreateTime: 2020/7/16 13:41
 */
object FormatUtil {
    /****
     * 计算文件大小
     *
     * @param length
     * @return
     */
    fun showLongFileSize(length: Long): String {
        return when {
            length >= 1073741824 -> String.format("%.2f", length / 1073741824F) + "GB"
            length >= 1048576 -> String.format("%.2f", length / 1048576F)+ "MB"
            length >= 1024 -> (length / 1024).toString() + "KB"
            length < 1024 -> length.toString() + "B"
            else -> "0KB"
        }
    }

    /**
     * 格式化时间戳
     */
    private val simpleDateFormat= SimpleDateFormat("yyyy-MM-dd   HH:mm", Locale.US)
    fun showTimeCalender(time: Long):String{
        return simpleDateFormat.format(Date(time))
    }

    /**
     * 把毫秒转换成：1:20:30这里形式
     * @param timeMs 毫秒单位
     * @return
     */
    fun stringForTime(timeMs: Long): String {
        // 转换成字符串的时间
        val mFormatBuilder = StringBuilder()
        val mFormatter = Formatter(mFormatBuilder, Locale.getDefault())

        val totalSeconds = timeMs / 1000
        val seconds = totalSeconds % 60


        val minutes = totalSeconds / 60 % 60


        val hours = totalSeconds / 3600


        mFormatBuilder.setLength(0)
        return mFormatter.format("%02d:%02d:%02d", hours, minutes, seconds).toString()
    }

    /**
     * 手机中间为*
     */
    fun phoneShowHide(phoneNum: String):String{
        if (!TextUtils.isEmpty(phoneNum) && phoneNum.length > 6) {
            val sb = java.lang.StringBuilder()
            for (i in phoneNum.indices) {
                val c: Char = phoneNum[i]
                if (i in 3..6) {
                    sb.append('*')
                } else {
                    sb.append(c)
                }
            }
            return sb.toString()
        }
        return phoneNum
    }
}