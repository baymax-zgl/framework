package com.guoliang.framekt.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.guoliang.framekt.R
import com.guoliang.framekt.util.permission_observable.PermissionObserver
import java.lang.ref.WeakReference
import java.util.*
import kotlin.collections.HashMap

/**
 * @Description: 权限管理工具类
 * @Author: zhangguoliang
 * @CreateTime: 2020/5/27 10:19
 */
object PermissionUtils {
    private const val REQUEST_CODE = 123
    private var isOpenPermission = false

    /**
     * 判断选权限
     *
     * @param activity    Activity
     * @param permissions 多个权限
     * @return
     */
    fun checkPermission(context: Context?, permissions: Array<String>): Boolean {
        val permissionsList: MutableList<String> = ArrayList()
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(context!!, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsList.add(permission)
            }
        }
        return permissionsList.isEmpty()
    }

    /**
     * 申请权限
     *
     * @param activity    Activity
     * @param permissions 多个权限
     */
    fun getPermission(activity: Activity?, permissions: Array<String>) {
        val permissionsList: MutableList<String> = ArrayList()
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(activity!!, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsList.add(permission)
            }
        }
        if (permissionsList.isEmpty()) {
            permissionObserverMap[activity.toString()]?.authorizationSuccess()
        } else {
            if (!isOpenPermission) {
                ActivityCompat.requestPermissions(activity!!, permissionsList.toTypedArray(), REQUEST_CODE)
            } else {
                permissionObserverMap[activity.toString()]?.authorizationFailure()
            }
        }
    }

    /**
     * 此方法必须再Activity中onRequestPermissionsResult调用
     * @param activity Activity
     * @param requestCode 返回值
     * @param permissions 返回权限集合
     * @param grantResults 是否授权集合
     */
    fun onRequestPermissionsResult(activity: Activity, requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        val permissionMap: MutableMap<String, String> = HashMap()
        val firstPermissionMap: MutableMap<String, Int> = HashMap()
        if (requestCode == REQUEST_CODE) {
            for (i in grantResults.indices) {
                firstPermissionMap[permissions[i]] = grantResults[i]
                //判断是否成功
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    //判断是否有禁止选项
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, permissions[i])) {
                        permissionMap[permissions[i]] = "forbid"
                    } else {
                        permissionMap[permissions[i]] = "allow"
                    }
                }
            }
        }
        if (permissionMap.isEmpty()) {
            permissionObserverMap[activity.toString()]?.authorizationSuccess()
            isOpenPermission = false
        } else {
            if (permissionMap.containsValue("forbid")) {
                showPermissionDialog(activity)
            } else {
                Toast.makeText(activity, activity.getString(R.string.please_permission), Toast.LENGTH_LONG).show()
                permissionObserverMap[activity.toString()]?.authorizationFailure()
                isOpenPermission = false
            }
        }
        permissionObserverMap[activity.toString()]?.firstAuthorization(firstPermissionMap)

    }

    private fun showPermissionDialog(activity: Activity) {
        AlertDialog.Builder(activity).setMessage(activity.getString(R.string.disable_the_prompt))
                .setPositiveButton(R.string.setting) { dialog, which ->
                    dialog.cancel()
                    val packageURI = Uri.parse("package:" + activity.packageName)
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI)
                    activity.startActivity(intent)
                }.setNegativeButton(R.string.cancel) { dialog, which -> dialog.cancel() }
                .setOnCancelListener {
                    permissionObserverMap[activity.toString()]?.authorizationFailure()
                    isOpenPermission = false
                }.create().show()
    }

    private val permissionObserverMap = HashMap<String, PermissionObserver>()
    fun setOnPermissionsListener(activity: Activity, permissionObserver: PermissionObserver): PermissionUtils {
        permissionObserverMap[activity.toString()] = permissionObserver
        return this
    }

    fun setOnPermissionsListener(activity: Activity, authorizationSuccess:(()->Unit)?=null,
                                                         authorizationFailure:(()->Unit)?=null,
                                                         firstAuthorization:((map: Map<String, Int>)->Unit)?=null
    ):PermissionUtils{
        val permissionObserver=object :PermissionObserver{
            override fun authorizationSuccess() {
                authorizationSuccess?.invoke()
            }

            override fun authorizationFailure() {
                authorizationFailure?.invoke()
            }

            override fun firstAuthorization(map: Map<String, Int>) {
                firstAuthorization?.invoke(map)
            }
        }
        setOnPermissionsListener(activity,permissionObserver)
        return this
    }

}