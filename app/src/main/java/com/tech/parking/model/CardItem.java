package com.tech.parking.model;

import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;

public interface CardItem {
    Drawable getDrawable();

    String getTitle();

    int getCounts();

    Fragment getFragment();
}
