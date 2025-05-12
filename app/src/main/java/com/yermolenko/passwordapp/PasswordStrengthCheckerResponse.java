package com.yermolenko.passwordapp;

import java.util.List;

public class PasswordStrengthCheckerResponse {
    private final StrengthLevel strength;
    private final List<String> suggestions;

    public PasswordStrengthCheckerResponse(StrengthLevel strength, List<String> suggestions) {
        this.strength = strength;
        this.suggestions = suggestions;
    }

    public StrengthLevel getStrength() {
        return strength;
    }

    public List<String> getSuggestions() {
        return suggestions;
    }
}
