package com.guoliang.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;


/**
 * Created by Administrator on 2017/6/1.
 * 我的页面选择ITEM
 */

public class CommItemView extends ConstraintLayout {

    private TextView item_right;
    private TextView title_text;

    public CommItemView(Context context) {
        super(context);
    }

    public CommItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 加载当前这个组合控件的布局文件
        View.inflate(context, R.layout.comm_item_view, this);
        // 获取当前组合控件中所有的元素
        ImageView imageView = (ImageView) findViewById(R.id.item_image);
        ImageView imgNav = (ImageView) findViewById(R.id.img_nav);
        title_text = (TextView) findViewById(R.id.item_title);
        item_right = (TextView) findViewById(R.id.item_right);
        View cut_off_rule = findViewById(R.id.cut_off_rule);
        // 加载自定义属性
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CommItemView);
        String title = a.getString(R.styleable.CommItemView_title_text);
        float titleSize = a.getDimension(R.styleable.CommItemView_title_text_size,title_text.getTextSize());
        String rightText = a.getString(R.styleable.CommItemView_right_text);
        Drawable backIcon = a.getDrawable(R.styleable.CommItemView_right_icon);
        Drawable icon = a.getDrawable(R.styleable.CommItemView_icon);
        boolean show_rule = a.getBoolean(R.styleable.CommItemView_show_rule, false);
        if (show_rule) {
            cut_off_rule.setVisibility(VISIBLE);
        } else {
            cut_off_rule.setVisibility(GONE);
        }
        if (backIcon!=null){
            imgNav.setBackground(backIcon);
        }else {
            imgNav.setBackgroundResource(R.mipmap.icon_right2);
        }

        // 将组合控件中元素和自定义的属性值进行绑定
        imageView.setImageDrawable(icon);
        title_text.setText(title);
        title_text.setTextSize(TypedValue.COMPLEX_UNIT_PX,titleSize);
        a.recycle();
    }

    public void setItem_right(String text) {
        item_right.setText(text);
    }

    public TextView getTitle_text() {
        return title_text;
    }
}
