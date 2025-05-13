package com.yermolenko.passwordapp;

import java.util.ArrayList;
import java.util.Arrays;
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

    // Common passwords list (small sample for demonstration)
    private static final List<String> COMMON_PASSWORDS = Arrays.asList(
            "123456", "123456789", "qwerty", "password", "12345", "12345678", "111111", "123123",
            "abc123", "1234567", "password1", "1234", "iloveyou", "1q2w3e4r", "admin", "welcome",
            "monkey", "1234567890", "letmein", "football", "princess", "qwertyuiop", "solo", "666666",
            "1qaz2wsx", "sunshine", "master", "123321", "qwerty123", "asdfghjkl", "starwars", "654321",
            "superman", "112233", "qazwsx", "121212", "dragon", "baseball", "michael", "shadow",
            "jessica", "000000", "password123", "trustno1", "hunter", "7777777", "qazxsw", "1qazxsw2",
            "whatever", "hello", "freedom", "charlie", "batman", "passw0rd", "jordan", "harley",
            "buster", "ginger", "thomas", "joshua", "cheese", "computer", "pepper", "asdf1234",
            "zxcvbnm", "102030", "qazwsxedc", "mickey", "daniel", "summer", "ashley", "987654321",
            "biteme", "banana", "michael1", "hockey", "cookie", "1q2w3e", "nicole", "tigger", "taylor",
            "andrew", "charles", "aaron431", "purple", "matrix", "secret", "scooby", "internet",
            "joseph", "pepper123", "snoopy", "hunter2", "blink182", "flower", "george", "merlin",
            "ranger", "williams", "dakota", "abcd1234", "1g2w3e4r", "pass123", "111222", "000111",
            "login", "michelle", "pass1", "loveme", "789456", "999999", "hannah", "amanda",
            "qwert", "pass", "zxcvb", "159753", "99999999", "aaaaaa", "abcdef", "turtle",
            "samsung", "qwe123", "patrick", "foobar", "pokemon", "test", "loveyou", "jennifer",
            "qweasd", "maggie", "pepper1", "777777", "basebal", "snowball", "football1", "tinkerbell",
            "121314", "198765", "a1b2c3", "1a2b3c", "klaster", "shadow1", "panther", "ashley1",
            "mustang", "12345a", "sunny", "888888", "passw0rd1", "silver", "mercedes", "babygirl",
            "cameron", "789123", "444444", "football123", "butterfly", "sydney", "peanut", "spider",
            "newyork", "jasper", "scooter", "tiffany", "marina", "justin", "heather", "angels",
            "william", "98765", "chocolate", "london", "buster1", "hockey1", "iloveu", "falcon",
            "america", "soccer", "nathan", "brandon", "madison", "garfield", "batman1", "samsung1",
            "sunshine1", "qwerty1", "school", "flower1", "cookies", "matrix1", "purple1", "internet1",
            "trustme", "computer1", "mustang1", "mypass", "dolphin", "cheyenne", "abcd123", "buster2",
            "dragon123", "123abc", "sunshine2", "qwerty1", "welcome1", "iloveyou1", "qwerty12", "123abc456",
            "password2", "letmein1", "qwerty1234", "1password", "qwerty789", "admin123", "baseball123",
            "iloveu123", "test123", "sunshine123", "secret123", "123qwerty", "pass1234", "11223344",
            "1qazxsw2", "iloveyou123", "hannah123", "tiger123", "dragonball", "1234qwert", "as123456",
            "diamond123", "hello123", "jordan123", "newyork123", "123abcqwert", "qwerty098", "password321",
            "asdfsdf", "111qwerty", "blue1234", "welcome123", "testqwert", "ilove123", "jessica123",
            "ilovemom", "love123", "mycat123", "cool123", "dragon12", "as12w3e", "spike123", "chicken123",
            "joshua123", "test1234", "starwars1", "bigdaddy123", "123p@ssw0rd", "dolphin123", "johnson123",
            "king123", "yankees123", "futbol123", "respect123", "loveme123", "vampire123", "123xyz",
            "tiger321", "purple123", "batman123", "william123", "hawaii123", "trustno123", "mystery123",
            "soccer1", "cameron123", "mercedes123", "tiger1", "fire123", "popcorn123", "mickey123",
            "diamond12", "robert123", "popcorn123", "green123", "piano123", "admin1234", "1234abcd",
            "9876asdf", "t3st123", "panther123", "diamond234", "michael2", "silver123", "golden123",
            "password00", "123ab321", "qwerty7890", "iloveyou321", "forever123", "gold123", "matrix123",
            "sunny1234", "marco123", "test321", "123stealth", "football1234", "kingof123", "monster123",
            "heather123", "paul123", "richard123", "123kjhf", "racecar123", "toby123", "cheese123",
            "fishing123", "garden123", "icecream123", "swimming123", "jumping123", "fall123", "newyear123",
            "1984tiger", "rocket123", "123ewrt", "key123", "cool1234", "jackson123", "chocolate123",
            "soccer123", "jack123", "superhero123", "123456abcd", "platinum123", "fishing1234", "happy1234",
            "lilith123", "rainbow123", "merlin123", "123dolphin", "hello1", "winter123", "friend123",
            "coffee123", "boston123", "summer123", "hawaiian123", "winter2015", "october123", "123banana",
            "moonlight123", "blackjack123", "september123", "april123", "pencil123", "sunshine04", "june1234",
            "apple123", "sunset123", "winterwinter", "myfamily123", "bright123", "grape123", "breeze123",
            "fallseason123", "penguin123", "summer2014", "hotdog123", "123rainbow", "sunnyday123", "oceanside123",
            "purplemoon123", "twin123", "seashell123", "cheerful123", "starry123", "january123", "october2025",
            "mystic123", "blueocean123", "july1234", "stormy123", "brilliant123", "tulip123", "alpine123",
            "november123", "mountain123", "melody123", "whisper123", "grapevine123", "summertime123", "cherry123",
            "light123", "flame123", "forest123", "river123", "breeze345", "country123", "elephant123", "robin123"
    );


    // Calculate entropy in bits: H = log2(N^L), where N is charset size, L is length
    private double calculateEntropy(String password) {
        int charsetSize = 0;
        if (UPPER.matcher(password).find()) charsetSize += 26; // Uppercase
        if (LOWER.matcher(password).find()) charsetSize += 26; // Lowercase
        if (DIGIT.matcher(password).find()) charsetSize += 10; // Digits
        if (SPECIAL.matcher(password).find()) charsetSize += 32; // Common special chars
        if (charsetSize == 0) return 0.0;
        return password.length() * Math.log(charsetSize) / Math.log(2);
    }

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
        boolean isCommonPassword = COMMON_PASSWORDS.contains(password.toLowerCase());
        double entropy = calculateEntropy(password);

        // Count character types
        int charTypes = 0;
        if (hasUpper) charTypes++;
        if (hasLower) charTypes++;
        if (hasDigit) charTypes++;
        if (hasSpecial) charTypes++;

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
        if (isCommonPassword) {
            suggestions.add("\uD83D\uDEA8 Avoid using common passwords like '" + password + "'. Choose something unique.");
        }
        if (entropy < 30 && length >= 6) {
            suggestions.add("\uD83D\uDD3A Increase complexity (e.g., mix more character types) to improve randomness.");
        }

        // Limit suggestions to 3 for clarity
        if (suggestions.size() > 3) {
            suggestions = suggestions.subList(0, 3);
        }

        // Determine strength
        StrengthLevel strength;
        if (isCommonPassword || length < 6 || charTypes <= 1 || hasRepeat || hasCommonPattern || entropy < 30) {
            strength = StrengthLevel.WEAK;
        } else if ((length >= 6 && length <= 10 && charTypes <= 3) || hasSpace || entropy < 50) {
            strength = StrengthLevel.MEDIUM;
        } else if (length >= 8 && length <= 11 && charTypes == 4 && !hasRepeat && !hasCommonPattern && !hasSpace && entropy >= 50) {
            strength = StrengthLevel.STRONG;
        } else if (length >= 12 && charTypes == 4 && !hasRepeat && !hasCommonPattern && !hasSpace && entropy >= 70) {
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