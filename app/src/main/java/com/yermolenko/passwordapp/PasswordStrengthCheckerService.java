package com.yermolenko.passwordapp;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class PasswordStrengthCheckerService {

    // Precompiled regex patterns for performance
    private static final Pattern UPPER = Pattern.compile("[A-Z]");
    private static final Pattern LOWER = Pattern.compile("[a-z]");
    private static final Pattern DIGIT = Pattern.compile("[0-9]");
    private static final Pattern SPECIAL = Pattern.compile("[!@#$%^&*(),.?\":{}|<>]");
    private static final Pattern REPEAT = Pattern.compile("(.)\\1{2,}"); // Detects 3+ repeated characters
    private static final Pattern COMMON_PATTERN = Pattern.compile("123|abc|qwe", Pattern.CASE_INSENSITIVE);

    public PasswordStrengthCheckerResponse checkPasswordStrength(String password) {
        if (password == null) {
            return new PasswordStrengthCheckerResponse(StrengthLevel.WEAK, List.of("Password cannot be null. ❗"));
        }

        // Initialize checks
        int length = password.length();
        boolean hasUpper = UPPER.matcher(password).find();
        boolean hasLower = LOWER.matcher(password).find();
        boolean hasDigit = DIGIT.matcher(password).find();
        boolean hasSpecial = SPECIAL.matcher(password).find();
        boolean hasRepeat = REPEAT.matcher(password).find();
        boolean hasCommonPattern = COMMON_PATTERN.matcher(password).find();
        boolean hasSpace = password.contains(" ");

        // Count character types
        int charTypes = 0;
        if (hasUpper)
            charTypes++;
        if (hasLower)
            charTypes++;
        if (hasDigit)
            charTypes++;
        if (hasSpecial)
            charTypes++;

        // Collect suggestions (prioritized and limited)
        List<String> suggestions = new ArrayList<>();
        if (length < 6) {
            suggestions.add("➕ Add " + (6 - length) + " more character(s) to reach 6.");
        } else if (length < 8) {
            suggestions.add("➕ Add " + (8 - length) + " more character(s) to reach 8 for a stronger password.");
        } else if (length < 12 && charTypes == 4) {
            suggestions.add("➕ Extend to 12 characters for a very strong password.");
        }

        if (charTypes <= 3) {
            if (!hasUpper)
                suggestions.add("\uD83D\uDD20 Include an uppercase letter (e.g., A, B) to improve strength.");
            if (!hasLower)
                suggestions.add("\uD83D\uDD21 Include a lowercase letter (e.g., a, b) to improve strength.");
            if (!hasDigit)
                suggestions.add("\uD83D\uDD22 Include a digit (e.g., 1, 2) to improve strength.");
            if (!hasSpecial)
                suggestions.add("\uD83D\uDD23 Include a special character (e.g., !, @, #) to improve strength.");
        }

        if (hasRepeat) {
            suggestions.add("\uD83D\uDD04 Avoid repeated characters (e.g., 'aaa') to make the password harder to guess.");
        }
        if (hasCommonPattern) {
            suggestions.add("\uD83D\uDEAB Avoid common sequences (e.g., '123', 'abc') for better security.");
        }
        if (hasSpace) {
            suggestions.add("❌ Remove spaces, as they may weaken the password.");
        }

        // Limit suggestions to 3 for clarity
        if (suggestions.size() > 3) {
            suggestions = suggestions.subList(0, 3);
        }

        // Determine strength
        StrengthLevel strength;
        if (length < 6 || charTypes <= 1 || hasRepeat || hasCommonPattern) {
            strength = StrengthLevel.WEAK;
        } else if ((length >= 6 && length <= 10 && charTypes <= 3) || hasSpace) {
            strength = StrengthLevel.MEDIUM;
        } else if (length >= 8 && length <= 11 && charTypes == 4 && !hasRepeat && !hasCommonPattern && !hasSpace) {
            strength = StrengthLevel.STRONG;
        } else if (length >= 12 && charTypes == 4 && !hasRepeat && !hasCommonPattern && !hasSpace) {
            strength = StrengthLevel.VERY_STRONG;
        } else {
            strength = StrengthLevel.MEDIUM;
        }

        // Add positive feedback for strong passwords
        if (strength == StrengthLevel.STRONG && suggestions.isEmpty()) {
            suggestions.add("✅ Good job! Your password is secure.");
        } else if (strength == StrengthLevel.VERY_STRONG && suggestions.isEmpty()) {
            suggestions.add("\uD83D\uDD12 Excellent! Your password is very secure.");
        }

        // Return response
        return new PasswordStrengthCheckerResponse(strength,
                suggestions.isEmpty() ? List.of("No suggestions needed!") : suggestions);
    }
}
