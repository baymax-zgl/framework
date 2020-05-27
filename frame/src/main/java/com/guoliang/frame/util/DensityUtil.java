package com.guoliang.frame.util;

import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.TypedValue;

/**
 * @Description:
 * @Author: zhangguoliang
 * @CreateTime: 2020/5/26 18:08
 */
public class DensityUtil {
    private DensityUtil() { }

    /**
     * DensityUtil实例
     */
    private static DensityUtil INSTANCE;

    public static DensityUtil getInstance(){
        if (INSTANCE==null){
            synchronized (DensityUtil.class){
                INSTANCE = new DensityUtil();
            }
        }
        return INSTANCE;
    }

    private Resources sResources=Resources.getSystem();

    private float dip2px(float dp){
        DisplayMetrics dm = sResources.getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dp,dm);
    }

    private int dip2pxInt(float dp){
        return (int) dip2px(dp);
    }

    private float sp2px(float sp){
        DisplayMetrics dm = sResources.getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,sp,dm);
    }

    private int sp2pxInt(float sp){
        return (int) sp2px(sp);
    }

    /**
     * 根据手机的分辨率PX(像素)转成DP
     * @param pxValue
     * @return
     */
    private int px2dip(Float pxValue) {
        float scale = sResources.getDisplayMetrics().density;
        return (int) (pxValue/scale+0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     * @param pxValue
     * @return
     */
    private int px2sp(Float pxValue) {
        float fontScale = sResources.getDisplayMetrics().scaledDensity;
        return (int) (pxValue/fontScale+0.5f);
    }
}
