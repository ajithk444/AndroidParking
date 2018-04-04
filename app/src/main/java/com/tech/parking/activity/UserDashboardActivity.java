package com.tech.parking.activity;

import android.support.v4.app.Fragment;

import com.tech.parking.base.DashboardActivity;
import com.tech.parking.beans.UserModel;
import com.tech.parking.fragments.user.UserHomeFragment;

public class UserDashboardActivity extends DashboardActivity {

    private UserHomeFragment homeFragment = new UserHomeFragment();

    @Override
    protected Fragment getStartupFragment() {
        homeFragment.setItemClick(this);
        return homeFragment;
    }

    @Override
    protected void updateUser(UserModel userModel) {
        homeFragment.updateUserState(userModel);
    }
}
