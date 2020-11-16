package com.guoliang.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @Author: zhangguoliang
 * @CreateTime: 2020/9/4 17:52
 */
public class CircleLayout extends ViewGroup {
    private int mRadius = 250;//子item的中心和整个layout中心的距离
    private double mChangeCorner = 90.0;
    List<Integer> newChildCount= new ArrayList<Integer>() ;

    public CircleLayout(Context context) {
        this(context,null);
    }

    public CircleLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CircleLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        final TypedArray a = context.obtainStyledAttributes(
                attrs, R.styleable.CircleLayout, defStyleAttr, defStyleAttr);

        mRadius = (int) a.getDimension(R.styleable.CircleLayout_radium, 250);
        mChangeCorner = a.getFloat(R.styleable.CircleLayout_changeCorner,90);
        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 计算出所有的childView的宽和高
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        //测量并保存layout的宽高(使用getDefaultSize时，wrap_content和match_parent都是填充屏幕)
        //稍后会重新写这个方法，能达到wrap_content的效果
        setMeasuredDimension( getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec),
                getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec));
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int childCount = getChildCount();
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        double corner;//旋转角度
        int childWidth;//item的宽
        int childHeight;//item的高
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == VISIBLE) {
                newChildCount.add(i);
            }
        }
        double averageCorner = 360D / newChildCount.size();
        for (int i = 0; i < newChildCount.size(); i++) {
            View child = getChildAt(newChildCount.get(i));
            corner=averageCorner*i;
            childWidth = child.getMeasuredWidth();
            childHeight = child.getMeasuredHeight();
            int cX = (int) (centerX - mRadius * Math.cos(Math.toRadians(corner+mChangeCorner )));
            int cY = (int) (centerY - mRadius * Math.sin(Math.toRadians(corner +mChangeCorner)));
            child.layout(cX - childWidth / 2, cY - childHeight / 2, cX + childWidth / 2, cY + childHeight / 2);
        }
        newChildCount.clear();
    }

}
