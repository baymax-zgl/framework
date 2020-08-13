package com.guoliang.framekt.util;

import android.view.View;
import android.view.ViewGroup;

/**
 * https://www.jianshu.com/p/6c32889e6377
 */
public class PopupWindowUtil {
    public static int makeDropDownMeasureSpec(int measureSpec) {
        int mode;
        if (measureSpec == ViewGroup.LayoutParams.WRAP_CONTENT) {
            mode = View.MeasureSpec.UNSPECIFIED;
        } else {
            mode = View.MeasureSpec.EXACTLY;
        }
        return View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(measureSpec), mode);
    }
}
