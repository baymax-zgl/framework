package com.example.framework;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.guoliang.frame.util.LogUtil;

/**
 * @Description:
 * @Author: zhangguoliang
 * @CreateTime: 2020/6/3 15:43
 */
public class User extends BaseObservable {
    private static final String TAG = "User";
    private String firstName;
    private String lastName;
    private Boolean isCheckUser=true;

    public User(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Bindable
    public String getFirstName() {
        return firstName;
    }

    @Bindable
    public String getLastName() {

        return lastName;
    }

    @Bindable
    public Boolean getCheckUser() {
        return isCheckUser;
    }

    public void setCheckUser(Boolean checkUser) {
        isCheckUser = checkUser;
        notifyPropertyChanged(BR.checkUser);
        LogUtil.getInstance().e(TAG, String.valueOf(checkUser));
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
        notifyPropertyChanged(BR.firstName);
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
        notifyPropertyChanged(BR.lastName);
    }

}
