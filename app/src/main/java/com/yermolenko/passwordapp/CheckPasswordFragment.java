package com.yermolenko.passwordapp;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputLayout;

public class CheckPasswordFragment extends Fragment {

    private EditText passwordInput;
    private TextInputLayout passwordInputLayout;
    private Button checkButton;
    private Button copyButton;
    private TextView strengthResult;
    private ListView suggestionsList;
    private PasswordStrengthCheckerService strengthCheckerService;
    private boolean isPasswordVisible = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_check_password, container, false);

        // Initialize UI components
        passwordInputLayout = view.findViewById(R.id.passwordInputLayout);
        passwordInput = view.findViewById(R.id.passwordInput);
        checkButton = view.findViewById(R.id.checkButton);
        copyButton = view.findViewById(R.id.copyButton);
        strengthResult = view.findViewById(R.id.strengthResult);
        suggestionsList = view.findViewById(R.id.suggestionsList);

        // Initialize service
        strengthCheckerService = new PasswordStrengthCheckerService();

        // Set up password visibility toggle
        passwordInputLayout.setEndIconOnClickListener(v -> togglePasswordVisibility());

        // Set up check button
        checkButton.setOnClickListener(v -> {
            String password = passwordInput.getText().toString();
            if (password.isEmpty()) {
                Toast.makeText(requireContext(), "Please enter a password", Toast.LENGTH_SHORT).show();
                return;
            }

            PasswordStrengthCheckerResponse response = strengthCheckerService.checkPasswordStrength(password);
            strengthResult.setText(getString(R.string.strength_result, response.getStrength().toString()));

            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    requireContext(),
                    android.R.layout.simple_list_item_1,
                    response.getSuggestions()
            );
            suggestionsList.setAdapter(adapter);
        });

        // Set up copy button
        copyButton.setOnClickListener(v -> {
            String password = passwordInput.getText().toString();
            if (!password.isEmpty()) {
                ClipboardManager clipboard = (ClipboardManager) requireContext().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Checked Password", password);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(requireContext(), R.string.password_copied, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(requireContext(), "No password to copy", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void togglePasswordVisibility() {
        isPasswordVisible = !isPasswordVisible;
        if (isPasswordVisible) {
            passwordInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            passwordInputLayout.setEndIconDrawable(R.drawable.ic_eye_off);
            passwordInputLayout.setEndIconContentDescription(R.string.hide_password);
        } else {
            passwordInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            passwordInputLayout.setEndIconDrawable(R.drawable.ic_eye);
            passwordInputLayout.setEndIconContentDescription(R.string.show_password);
        }
        passwordInput.setSelection(passwordInput.getText().length());
    }
}
