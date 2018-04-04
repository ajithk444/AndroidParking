package com.tech.parking.validation;

import android.util.Patterns;

public class TextValidation {

    private static final int MIN_PASS_LENGTH = 6;
    private static final int MIN_NAME_LENGTH = 5;

    public static Boolean isValidPassword(String password) {
        return isNotNull(password) && password.trim().length() > MIN_PASS_LENGTH;
    }

    public static Boolean isValidEmail(String email) {
        return isNotNull(email) && isEmail(email);
    }

    public static Boolean isValidName(String name) {
        return isNotNull(name) && name.length() >= MIN_NAME_LENGTH;
    }

    private static boolean isNotNull(String text) {
        return text != null;
    }

    private static boolean isEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).find();
    }

    public static boolean isValidMobileNumber(String mobileNumber) {
        return isNotNull(mobileNumber) && Patterns.PHONE.matcher(mobileNumber).find();
    }
}
