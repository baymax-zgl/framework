package com.guoliang.framekt.util;

import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.TypedValue;

/**
 * @Description:
 * @Author: zhangguoliang
 * @CreateTime: 2020/5/26 18:08
 */
public class DensityUtil {

    private static Resources sResources=Resources.getSystem();

    public static float dip2px(float dp){
        DisplayMetrics dm = sResources.getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dp,dm);
    }

    public static int dip2pxInt(float dp){
        return (int) dip2px(dp);
    }

    public static float sp2px(float sp){
        DisplayMetrics dm = sResources.getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,sp,dm);
    }

    public static int sp2pxInt(float sp){
        return (int) sp2px(sp);
    }

    /**
     * 根据手机的分辨率PX(像素)转成DP
     * @param pxValue
     * @return
     */
    public static int px2dip(Float pxValue) {
        float scale = sResources.getDisplayMetrics().density;
        return (int) (pxValue/scale+0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     * @param pxValue
     * @return
     */
    public static int px2sp(Float pxValue) {
        float fontScale = sResources.getDisplayMetrics().scaledDensity;
        return (int) (pxValue/fontScale+0.5f);
    }
}
