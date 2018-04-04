package com.tech.parking.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.tech.parking.WelcomeActivity;
import com.tech.parking.beans.UserModel;
import com.tech.parking.controller.authorize.UserController;

public abstract class BaseActivity extends AppCompatActivity {

    private UserModel userModel;

    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getActivityLayout());
        init(savedInstanceState);
    }

    protected abstract void init(Bundle bundle);

    protected abstract int getActivityLayout();

    public final String getUserId() {
        return UserController.getInstance().getUserId();
    }

    public final UserModel getUserModel() {
        initUserModel();
        return userModel;
    }

    private void initUserModel() {
        if (userModel == null && (userModel = UserModel.getCurrentInstance()) == null)
            userModel = (UserModel) getIntent().getSerializableExtra(WelcomeActivity.USER_MODEL);
    }
}
