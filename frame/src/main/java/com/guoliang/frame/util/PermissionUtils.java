package com.guoliang.frame.util;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.guoliang.frame.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 权限管理工具类
 * @Author: zhangguoliang
 * @CreateTime: 2020/5/27 10:19
 */
public class PermissionUtils {
    private PermissionUtils() { }

    /**
     * PermissionUtils实例
     */
    private static PermissionUtils INSTANCE;

    public static PermissionUtils getInstance() {
        if (INSTANCE == null) {
            synchronized (PermissionUtils.class) {
                INSTANCE = new PermissionUtils();
            }
        }
        return INSTANCE;
    }

    private static final int REQUEST_CODE = 100;


    /**
     * 判断选权限
     *
     * @param activity    Activity
     * @param permissions 多个权限
     * @return
     */
    public boolean checkPermission(Activity activity, String... permissions) {
        List<String> permissionsList = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsList.add(permission);
            }
        }
        return permissionsList.isEmpty();
    }

    /**
     * 申请权限
     *
     * @param activity    Activity
     * @param permissions 多个权限
     */
    public void getPermission(Activity activity, String[] permissions) {
        List<String> permissionsList = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsList.add(permission);
            }
        }
        if (permissionsList.isEmpty()) {
            if (onPermissionsListener != null) {
                onPermissionsListener.authorizationSuccess();
            }
        } else {
            String[] strings = new String[permissionsList.size()];
            ActivityCompat.requestPermissions(activity, permissionsList.toArray(strings), REQUEST_CODE);
        }
    }

    /**
     * 此方法必须再Activity中onRequestPermissionsResult调用
     * @param activity Activity
     * @param requestCode 返回值
     * @param permissions 返回权限集合
     * @param grantResults 是否授权集合
     */
    public void onRequestPermissionsResult(Activity activity, int requestCode, String[] permissions, int[] grantResults) {
        Map<String, String> permissionMap = new HashMap<>();
        if (requestCode == REQUEST_CODE) {
            for (int i = 0; i < grantResults.length; i++) {
                //判断是否成功
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    //判断是否有禁止选项
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, permissions[i])) {
                        permissionMap.put(permissions[i], "forbid");
                    } else {
                        permissionMap.put(permissions[i], "allow");
                    }
                }
            }
        }
        if (permissionMap.isEmpty()) {
            if (onPermissionsListener != null) {
                onPermissionsListener.authorizationSuccess();
            }
        } else {
            if (permissionMap.containsValue("forbid")) {
                showPermissionDialog(activity);
            } else {
                Toast.makeText(activity, activity.getString(R.string.please_permission), Toast.LENGTH_LONG).show();
            }
            if (onPermissionsListener != null) {
                onPermissionsListener.authorizationFailure();
            }
        }
    }

    private void showPermissionDialog(final Activity activity) {
        new AlertDialog.Builder(activity).setMessage(activity.getString(R.string.disable_the_prompt))
                .setPositiveButton(R.string.setting, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        Uri packageURI = Uri.parse("package:" + activity.getPackageName());
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
                        activity.startActivity(intent);
                    }
                }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        }).create().show();
    }


    private OnPermissionsListener onPermissionsListener;

    public void setOnPermissionsListener(OnPermissionsListener onPermissionsListener) {
        this.onPermissionsListener = onPermissionsListener;
    }

    public interface OnPermissionsListener {
        //授权成功
        void authorizationSuccess();

        //授权失败
        void authorizationFailure();
    }


}
