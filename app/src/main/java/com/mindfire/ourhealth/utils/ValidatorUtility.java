package com.mindfire.ourhealth.utils;

import android.util.Patterns;

/**
 * This class is used for validating fields of various modules.
 */
public class ValidatorUtility {
    private static final String VALID_PASSWORD ="^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,20}$";
    private static final String VALID_NAME = "[a-zA-Z]+$";
    private static final String VALID_NUMBER = "\\d+";

    /**
     * Validates email address otherwise display error.
     */
    public static boolean isEmailValid(String iEmailString) {
        return iEmailString == null || !Patterns.EMAIL_ADDRESS.matcher(iEmailString).matches();
    }

    /**
     * Validates password pattern, otherwise display error.
     */
    public static boolean isPasswordValid(String iPasswordString) {
        return iPasswordString == null || !iPasswordString.matches(VALID_PASSWORD);
    }

    /**
     * Validates if a text field is null.
     */
    public static boolean isTextFieldEmpty(int iLength) {
        return iLength == 0;
    }

    public static boolean isValidName(String iName) {
        return iName == null || !iName.matches(VALID_NAME);
    }

    public static boolean isPhoneNumber(String iNumber) {
        return iNumber == null || !Patterns.PHONE.matcher(iNumber).matches();
    }

    public static boolean isNumber(String iNumber) {
        return iNumber == null || !iNumber.matches(VALID_NUMBER);
    }
}
