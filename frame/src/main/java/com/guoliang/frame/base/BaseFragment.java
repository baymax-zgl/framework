package com.guoliang.frame.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * @Description:
 * @Author: zhangguoliang
 * @CreateTime: 2020/5/27 11:17
 */
abstract class BaseFragment extends Fragment {

    //布局id
    abstract int getLayoutId();
    //页面名称
    abstract String getPageName();
    //初始化
    abstract void initView(View fragmentView,Bundle savedInstanceState);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(getLayoutId(),container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view,savedInstanceState);
    }

    public void toast(String test){
        Toast.makeText(getContext(), test, Toast.LENGTH_SHORT).show();
    }
}
