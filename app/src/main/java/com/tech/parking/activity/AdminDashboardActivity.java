package com.tech.parking.activity;

import android.support.v4.app.Fragment;

import com.tech.parking.base.DashboardActivity;
import com.tech.parking.fragments.admin.AdminHomeFragment;

public class AdminDashboardActivity extends DashboardActivity {

    private AdminHomeFragment homeFragment = new AdminHomeFragment();

    @Override
    protected Fragment getStartupFragment() {
        homeFragment.setItemClick(this);
        return homeFragment;
    }
}
