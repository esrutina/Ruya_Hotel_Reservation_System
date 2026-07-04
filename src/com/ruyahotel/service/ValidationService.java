package com.ruyahotel.service;

import java.util.regex.Pattern;

public class ValidationService {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\+?[0-9]{10,15}$");
    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[A-Za-z0-9_]{4,20}$");

    public static boolean isNotEmpty(String s) {
        return s != null && !s.trim().isEmpty();
    }

    public static boolean isValidEmail(String email) {
        return isNotEmpty(email) && EMAIL_PATTERN.matcher(email).matches();
    }

    public static boolean isValidPhone(String phone) {
        return isNotEmpty(phone) && PHONE_PATTERN.matcher(phone).matches();
    }

    public static boolean isValidUsername(String username) {
        return isNotEmpty(username) && USERNAME_PATTERN.matcher(username).matches();
    }

    public static boolean isStrongPassword(String password) {
        if (!isNotEmpty(password) || password.length() < 6) return false;
        boolean hasUpper = false, hasLower = false, hasDigit = false;
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) hasUpper = true;
            if (Character.isLowerCase(c)) hasLower = true;
            if (Character.isDigit(c)) hasDigit = true;
        }
        return hasUpper && hasLower && hasDigit;
    }

    public static boolean isPositiveNumber(double value) {
        return value > 0;
    }

    public static boolean isValidDateRange(java.time.LocalDate start, java.time.LocalDate end) {
        return start != null && end != null && end.isAfter(start);
    }

    public static String validateRegistration(String firstName, String lastName, String username, String email,
                                              String phone, String password, String confirm) {
        if (!isNotEmpty(firstName)) return "First name is required";
        if (!isNotEmpty(lastName)) return "Last name is required";
        if (!isValidUsername(username)) return "Username must be 4-20 characters (letters, numbers, underscores)";
        if (!isValidEmail(email)) return "Please enter a valid email address";
        if (!isValidPhone(phone)) return "Please enter a valid phone number";
        if (!isStrongPassword(password)) return "Password must be at least 6 chars with uppercase, lowercase, and digit";
        if (!password.equals(confirm)) return "Passwords do not match";
        return null;
    }
}
