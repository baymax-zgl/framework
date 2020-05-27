package com.guoliang.frame.view;

import android.app.Dialog;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @Description:
 * @Author: zhangguoliang
 * @CreateTime: 2020/5/27 11:23
 */
public class SafeDialog extends Dialog {

    public SafeDialog(@NonNull Context context) {
        super(context);
    }

    public SafeDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected SafeDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    public void show() {
        try {
            if (!isShowing()) {
                super.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void dismiss() {
        try {
            if (isShowing()) {
                super.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
