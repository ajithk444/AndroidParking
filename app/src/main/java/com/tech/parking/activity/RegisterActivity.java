package com.tech.parking.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

import com.tech.parking.R;
import com.tech.parking.WelcomeActivity;
import com.tech.parking.beans.UserModel;
import com.tech.parking.beans.UserType;
import com.tech.parking.controller.authorize.UserController;
import com.tech.parking.listeners.RequestCallback;
import com.tech.parking.utils.ToastUtils;
import com.tech.parking.validation.TextValidation;

public class RegisterActivity extends AppCompatActivity {
    private EditText registerPassword,
            registerEmailAddress,
            registerMobileNumber,
            registerFullName;
    private Button login,
            register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerEmailAddress = findViewById(R.id.registerEmailAddress);
        registerPassword = findViewById(R.id.registerPassword);
        registerFullName = findViewById(R.id.registerFullName);
        registerMobileNumber = findViewById(R.id.registerMobileNumber);
        register = findViewById(R.id.registerButton);
        login = findViewById(R.id.goToLoginButton);
        register.setOnClickListener(view -> {
            String email = registerEmailAddress.getText().toString();
            String name = registerFullName.getText().toString();
            String password = registerPassword.getText().toString();
            String mobileNumber = registerMobileNumber.getText().toString();
            if (!TextValidation.isValidPassword(password)) {
                ToastUtils.showToastError(this, "Please Enter Valid Password! Length must be > 6");
            } else if (!TextValidation.isValidEmail(email)) {
                ToastUtils.showToastError(this, "Please Enter Valid Email !");
            } else if (!TextValidation.isValidName(name)) {
                ToastUtils.showToastError(this, "Please Enter Valid Name ! Length must be > 5");
            } else if (!TextValidation.isValidMobileNumber(mobileNumber)) {
                ToastUtils.showToastError(this, "Please Enter Valid Mobile Number !");
            } else {
                UserModel userModel = new UserModel();
                userModel.setUserBalance(0.0);
                userModel.setEmailAddress(email);
                userModel.setFullName(name);
                userModel.setMobileNumber(mobileNumber);
                userModel.setTotalBooking(0);
                userModel.setTotalCar(0);
                userModel.setTotalHistory(0);
                userModel.setTotalUserCharge(0);
                userModel.setUserType(UserType.USER);
                startRegister(password, userModel);
            }
        });

        login.setOnClickListener(view -> {
            goToLoginActivity();
        });
    }

    private void goToLoginActivity() {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void startRegister(String pass, UserModel userModel) {
        makeEnabled(false);
        ToastUtils.showToastInfo(this, "Starting Register...");
        UserController.getInstance().register(userModel, pass, new RequestCallback<String>() {
            @Override
            public void success(String userId) {
                ToastUtils.showToastSuccess(RegisterActivity.this, "Successfully Registered");
                Intent intent = new Intent(RegisterActivity.this, UserDashboardActivity.class);
                userModel.setUserId(userId);
                UserModel.init(userModel);
                intent.putExtra(WelcomeActivity.USER_MODEL, userModel);
                startActivity(intent);
                finish();
            }

            @Override
            public void error(String error) {
                ToastUtils.showToastError(RegisterActivity.this, error);
                makeEnabled(true);
            }
        });
    }

    private void makeEnabled(boolean enabled) {
        login.setEnabled(enabled);
        register.setEnabled(enabled);
        registerEmailAddress.setEnabled(enabled);
        registerFullName.setEnabled(enabled);
        registerMobileNumber.setEnabled(enabled);
        registerPassword.setEnabled(enabled);
    }

    @FunctionalInterface
    public interface Supplier<T> {
        T get();
    }
}
