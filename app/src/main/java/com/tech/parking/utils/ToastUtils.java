package com.tech.parking.utils;

import android.app.Activity;
import android.content.Context;

import com.tapadoo.alerter.Alerter;
import com.tech.parking.R;

public class ToastUtils {

    private static void showToast(Context context, CharSequence text, int colorRes, String title) {
        if (context != null)
            Alerter.create((Activity) context)
                    .setDuration(500)
                    .setBackgroundColorRes(colorRes)
                    .setTitle(title)
                    .setText(text.toString())
                    .show();
    }

    public static void showToastError(Context context, CharSequence text) {
        showToast(context, text, R.color.red, "Error");
    }

    public static void showToastInfo(Context context, CharSequence text) {
        Alerter.create((Activity) context)
                .enableProgress(true)
                .setBackgroundColorRes(android.R.color.darker_gray)
                .setTitle("Info")
                .setText(text.toString())
                .show();
    }

    public static void showToastInfo(Context context, CharSequence text, int duration) {
        showToast(context, text, android.R.color.darker_gray, "Info");
    }

    public static void showToastSuccess(Context context, CharSequence text) {
        showToast(context, text, R.color.colorPrimaryDark, "Success");
    }

    public static void showToastWarning(Context context, CharSequence text) {
        showToast(context, text, android.R.color.holo_orange_dark, "Warning");
    }

}
