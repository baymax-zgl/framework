package com.guoliang.customview;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.google.android.material.appbar.AppBarLayout;

/**
 * Created by guoliang on 2018/4/27.
 */

public class MyToolbar extends AppBarLayout {
    private ImageView right_image;
    private ImageView left_image;
    private TextView app_title;
    private TextView right_text;
    private TextView left_text;

    public MyToolbar(Context context) {
        super(context);
    }

    public MyToolbar(final Context context, AttributeSet attrs) {
        super(context, attrs);
        View.inflate(context, R.layout.toolbar_layout, this);
        right_image = (ImageView) findViewById(R.id.right_image);
        left_text = (TextView) findViewById(R.id.left_text);
        left_image = (ImageView) findViewById(R.id.left_image);
        app_title = (TextView) findViewById(R.id.app_title);
        right_text = (TextView) findViewById(R.id.right_text);
        AppBarLayout app_bar = (AppBarLayout) findViewById(R.id.app_bar);
        // 加载自定义属性
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MyToolbar);
        String right_string = a.getString(R.styleable.MyToolbar_right_title);
        String left_string = a.getString(R.styleable.MyToolbar_left_title);
        String app_string = a.getString(R.styleable.MyToolbar_app_title);
        boolean left_image_null = a.getBoolean(R.styleable.MyToolbar_left_image_null, false);
        float right_padding = a.getDimension(R.styleable.MyToolbar_right_padding,left_image.getPaddingLeft());
        Drawable left_draw = a.getDrawable(R.styleable.MyToolbar_left_image);
        Drawable right_draw = a.getDrawable(R.styleable.MyToolbar_right_image);
        float left_padding = a.getDimension(R.styleable.MyToolbar_left_padding,left_image.getPaddingLeft());
        Drawable app_bar_drawable = a.getDrawable(R.styleable.MyToolbar_app_bar_drawable);
        int right_title_color = a.getColor(R.styleable.MyToolbar_right_title_color, Color.parseColor("#ffffff"));
        int app_bar_color = a.getColor(R.styleable.MyToolbar_app_bar_color, Color.parseColor("#ffffff"));
        int app_title_color = a.getColor(R.styleable.MyToolbar_app_title_color, Color.parseColor("#000000"));
        if (app_bar_drawable==null) {
            app_bar.setBackgroundColor(app_bar_color);
        }else {
            app_bar.setBackground(app_bar_drawable);
        }
        if (right_draw == null) right_image.setVisibility(View.GONE);
        if (left_string == null) left_text.setVisibility(View.GONE);
        if (right_string == null) right_text.setVisibility(View.GONE);
        right_image.setImageDrawable(right_draw);
        left_text.setTextColor(right_title_color);
        right_text.setTextColor(right_title_color);
        app_title.setTextColor(app_title_color);
        if (left_draw == null) {
            left_image.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.icon_fanhui_black));
        } else {
            left_image.setPadding((int) left_padding,0,0,0);
            left_image.setImageDrawable(left_draw);
        }
        if (right_draw != null) {
            right_image.setPadding(0,0,(int) right_padding,0);
            right_image.setImageDrawable(right_draw);
        }
        left_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Activity activity = (Activity) context;
                activity.finish();
            }
        });
        if (left_image_null) {
            left_image.setVisibility(View.GONE);
        }
        app_title.setText(app_string);
        left_text.setText(left_string);
        right_text.setText(right_string);
        a.recycle();
    }

    public ImageView getRight_image() {
        return right_image;
    }

    public ImageView getLeft_image() {
        return left_image;
    }

    public TextView getApp_title() {
        return app_title;
    }

    public TextView getRight_text() {
        return right_text;
    }

    public TextView getLeft_text() {
        return left_text;
    }

    public void setApp_title(String text) {
        app_title.setText(text);
    }

    public void setRight_title(String text) {
        right_text.setText(text);
        right_text.setVisibility(View.VISIBLE);
    }

    public interface OnBackClickListener {
        void setOnBackClickListener();
    }
}
