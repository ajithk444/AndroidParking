package com.tech.parking.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.tech.parking.R;


public class FragmentController {
    private final Integer container;
    private FragmentManager fragmentManager;
    private Fragment fragment;

    public FragmentController(Integer container, FragmentManager fragmentManager) {
        this.container = container;
        this.fragmentManager = fragmentManager;
    }

    public void changeFragment(Fragment fragment) {
        this.fragment = fragment;
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(container, fragment);
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
        fragmentTransaction.commit();
    }

    public Fragment getCurrent() {
        return fragment;
    }
}
