package com.yermolenko.passwordapp;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.slider.Slider;

public class GeneratePasswordFragment extends Fragment {

    private TextView passwordDisplay;
    private CheckBox lowercaseCheck, uppercaseCheck, digitsCheck, symbolsCheck;
    private Slider lengthSlider;
    private TextView lengthLabel;
    private Button generateButton;
    private Button copyButton;
    private PasswordGeneratorService generatorService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_generate_password, container, false);

        // Initialize UI components
        passwordDisplay = view.findViewById(R.id.passwordDisplay);
        lowercaseCheck = view.findViewById(R.id.lowercaseCheck);
        uppercaseCheck = view.findViewById(R.id.uppercaseCheck);
        digitsCheck = view.findViewById(R.id.digitsCheck);
        symbolsCheck = view.findViewById(R.id.symbolsCheck);
        lengthSlider = view.findViewById(R.id.lengthSlider);
        lengthLabel = view.findViewById(R.id.lengthLabel);
        generateButton = view.findViewById(R.id.generateButton);
        copyButton = view.findViewById(R.id.copyButton);

        // Initialize service
        generatorService = new PasswordGeneratorService();

        // Update length label when slider changes
        lengthSlider.addOnChangeListener((slider, value, fromUser) -> {
            int length = (int) value;
            lengthLabel.setText(getString(R.string.length_label, length));
        });

        // Set initial length label
        lengthLabel.setText(getString(R.string.length_label, (int) lengthSlider.getValue()));

        // Set up generate button
        generateButton.setOnClickListener(v -> {
            try {
                int length = (int) lengthSlider.getValue();
                String password = generatorService.generatePassword(
                        length,
                        lowercaseCheck.isChecked(),
                        uppercaseCheck.isChecked(),
                        digitsCheck.isChecked(),
                        symbolsCheck.isChecked()
                );
                passwordDisplay.setText(password);
            } catch (IllegalArgumentException e) {
                Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Set up copy button
        copyButton.setOnClickListener(v -> {
            String password = passwordDisplay.getText().toString();
            if (!password.isEmpty()) {
                ClipboardManager clipboard = (ClipboardManager) requireContext().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Generated Password", password);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(requireContext(), R.string.password_copied, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(requireContext(), "No password to copy", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
