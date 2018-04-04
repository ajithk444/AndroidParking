package com.tech.parking;

import android.app.Application;

public class ParkingApp extends Application {
    private static ParkingApp instance;

    public static ParkingApp getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

}
