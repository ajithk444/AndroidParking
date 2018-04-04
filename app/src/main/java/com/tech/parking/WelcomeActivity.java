package com.tech.parking;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.tech.parking.activity.AdminDashboardActivity;
import com.tech.parking.activity.LoginActivity;
import com.tech.parking.activity.UserDashboardActivity;
import com.tech.parking.beans.UserModel;
import com.tech.parking.beans.UserType;
import com.tech.parking.controller.authorize.UserController;
import com.tech.parking.listeners.RequestCallback;

public class WelcomeActivity extends AppCompatActivity {

    public static final String TAG = WelcomeActivity.class.getName();
    public static final String USER_MODEL = "USER_MODEL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        new Handler().post(this::validateCurrentUser);
    }

    private void validateCurrentUser() {
        UserController.getInstance().findCurrentUser(new RequestCallback<UserModel>() {
            @Override
            public void success(UserModel model) {
                Log.d(TAG, "onFound: " + model.getUserType());
                if (model.getUserType().equals(UserType.USER)) {
                    gotoActivity(UserDashboardActivity.class, model);
                } else {
                    gotoActivity(AdminDashboardActivity.class, model);
                }
            }

            @Override
            public void error(String error) {
                Log.d(TAG, "onError: " + error);
                gotoActivity(LoginActivity.class, null);
            }
        });
    }

    private void gotoActivity(Class<? extends Activity> activityClass, UserModel model) {
        Intent intent = new Intent(this, activityClass);
        if (model != null) {
            intent.putExtra(USER_MODEL, model);
            UserModel.init(model);
        }
        startActivity(intent);
        finish();
    }
}
