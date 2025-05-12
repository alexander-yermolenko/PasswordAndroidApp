package com.yermolenko.passwordapp;

import androidx.annotation.NonNull;

import java.util.Map;
import java.util.Objects;

public enum StrengthLevel {
    WEAK, MEDIUM, STRONG, VERY_STRONG;

    private static final Map<StrengthLevel, String> DISPLAY_MAP = Map.of(
            WEAK, "Weak 🤒",
            MEDIUM, "Medium 😐",
            STRONG, "Strong 😎",
            VERY_STRONG, "Very strong 🤫"
    );

    @NonNull
    @Override
    public String toString() {
        return Objects.requireNonNull(DISPLAY_MAP.getOrDefault(this, name().toLowerCase()));
    }
}
