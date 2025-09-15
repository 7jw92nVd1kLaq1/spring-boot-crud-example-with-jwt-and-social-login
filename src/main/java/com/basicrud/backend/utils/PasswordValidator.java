package com.basicrud.backend.utils;

import org.springframework.stereotype.Component;

@Component
public class PasswordValidator {
    public boolean isValid(String password) {
        if (password == null || password.length() < 8 || password.length() > 1024) {
            return false; // Minimum length requirement
        }

        boolean hasUpper = false;
        boolean hasLower = false;
        boolean hasDigit = false;
        boolean hasSpecial = false;

        for (char ch : password.toCharArray()) {
            if (Character.isUpperCase(ch)) hasUpper = true;
            else if (Character.isLowerCase(ch)) hasLower = true;
            else if (Character.isDigit(ch)) hasDigit = true;
            else if ("!@#$%^&*()-+".indexOf(ch) >= 0) hasSpecial = true;

            if (hasUpper && hasLower && hasDigit && hasSpecial) {
                return true; // All conditions met
            }
        }

        return false; // If no valid password found
    }
}
