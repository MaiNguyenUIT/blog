package com.example.backend.utils;

import java.util.regex.Pattern;

public class ValidationAccount {
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

    private static final String PASSWORD_REGEX = "^[A-Z][A-Za-z0-9]*[!@#$%^&*()_+=<>?/{}\\[\\]-]+[A-Za-z0-9]*$";

    public static boolean isValidEmail(String email) {
        return email != null && Pattern.matches(EMAIL_REGEX, email);
    }

    public static boolean isValidPassword(String password) {
        return password != null && Pattern.matches(PASSWORD_REGEX, password);
    }
    public static boolean isValidUserName(String username) {
        if (username == null || username.trim().isEmpty()) {
            return false;
        }

        String regex = "^[a-zA-Z0-9_]{3,20}$";
        return username.matches(regex);
    }
}
