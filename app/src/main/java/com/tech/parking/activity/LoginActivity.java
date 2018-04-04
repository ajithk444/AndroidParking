package com.tech.parking.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

import com.tech.parking.R;
import com.tech.parking.WelcomeActivity;
import com.tech.parking.listeners.RequestCallback;
import com.tech.parking.controller.authorize.UserController;
import com.tech.parking.beans.UserModel;
import com.tech.parking.beans.UserType;
import com.tech.parking.utils.ToastUtils;
import com.tech.parking.validation.TextValidation;

public class LoginActivity extends AppCompatActivity {
    private EditText loginPassword, loginEmailAddress;
    private Button login, register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginEmailAddress = findViewById(R.id.loginEmailAddress);
        loginPassword = findViewById(R.id.loginPassword);
        login = findViewById(R.id.loginButton);
        register = findViewById(R.id.goToRegisterButton);
        login.setOnClickListener(view -> {
            String email = loginEmailAddress.getText().toString();
            String password = loginPassword.getText().toString();
            if (TextValidation.isValidEmail(email) && TextValidation.isValidPassword(password)) {
                loginStart(email, password);
            } else {
                ToastUtils.showToastError(LoginActivity.this, "Please Fill All Fields !");
            }
        });

        register.setOnClickListener(view -> {
            goToRegisterActivity();
        });
    }

    private void goToRegisterActivity() {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
        finish();
    }

    private void loginStart(String email, String password) {
        ToastUtils.showToastInfo(this, "Starting Login...");
        makeEnabled(false);
        UserController.getInstance().login(email, password, new RequestCallback<UserModel>() {
            @Override
            public void success(UserModel userModel) {
                Intent intent;
                if (userModel.getUserType().equals(UserType.USER)) {
                    intent = new Intent(LoginActivity.this, UserDashboardActivity.class);
                } else {
                    intent = new Intent(LoginActivity.this, AdminDashboardActivity.class);
                }
                UserModel.init(userModel);
                intent.putExtra(WelcomeActivity.USER_MODEL, userModel);
                startActivity(intent);
                finish();
            }

            @Override
            public void error(String error) {
                makeEnabled(true);
                ToastUtils.showToastError(LoginActivity.this, error);
            }
        });
    }

    private void makeEnabled(boolean enabled) {
        login.setEnabled(enabled);
        register.setEnabled(enabled);
        loginEmailAddress.setEnabled(enabled);
        loginPassword.setEnabled(enabled);
    }
}
