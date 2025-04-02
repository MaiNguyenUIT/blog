package com.example.backend.utils;

import java.util.regex.Pattern;

public class ValidationAccount {
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

    // Regex kiểm tra mật khẩu (Chữ cái in hoa đầu tiên + ít nhất 1 ký tự đặc biệt)
    private static final String PASSWORD_REGEX = "^[A-Z][A-Za-z0-9]*[!@#$%^&*()_+=<>?/{}\\[\\]-]+[A-Za-z0-9]*$";

    public static boolean isValidEmail(String email) {
        return email != null && Pattern.matches(EMAIL_REGEX, email);
    }

    public static boolean isValidPassword(String password) {
        return password != null && Pattern.matches(PASSWORD_REGEX, password);
    }
}
