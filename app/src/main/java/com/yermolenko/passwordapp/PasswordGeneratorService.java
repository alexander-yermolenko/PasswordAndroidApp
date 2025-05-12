package com.yermolenko.passwordapp;

import java.security.SecureRandom;

public class PasswordGeneratorService {
    private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String DIGITS = "0123456789";
    private static final String SYMBOLS = "!@#$%^&*()_+-=[]{}|;:,.<>?";

    public String generatePassword(int length, boolean useLowercase, boolean useUppercase, boolean useDigits,
                                   boolean useSymbols) {
        if (length <= 0) {
            throw new IllegalArgumentException("Password length must be positive");
        }

        StringBuilder charPool = new StringBuilder();
        if (useLowercase)
            charPool.append(LOWERCASE);
        if (useUppercase)
            charPool.append(UPPERCASE);
        if (useDigits)
            charPool.append(DIGITS);
        if (useSymbols)
            charPool.append(SYMBOLS);

        if (charPool.length() == 0) {
            throw new IllegalArgumentException("At least one character type must be selected");
        }

        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(charPool.length());
            password.append(charPool.charAt(index));
        }

        return password.toString();
    }
}
