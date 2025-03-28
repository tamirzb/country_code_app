package com.tamirzb.countrycodeapp;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private EditText codeInput;
    private TextView countryDisplay;
    private TextView emojiDisplay;
    private HashMap<String, String> countryCodes;
    private HashMap<String, String> countryEmojis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI elements
        codeInput = findViewById(R.id.code_input);
        countryDisplay = findViewById(R.id.country_display);
        emojiDisplay = findViewById(R.id.emoji_display);
        Button clearButton = findViewById(R.id.clear_button);
        
        clearButton.setOnClickListener(v -> {
            codeInput.setText("");
            countryDisplay.setText("");
            emojiDisplay.setText("");
            codeInput.requestFocus();
        });

        // Initialize country maps
        countryCodes = new HashMap<>();
        countryEmojis = new HashMap<>();
        loadCountryCodesFromCSV();

        // Set focus to EditText to show number pad immediately
        codeInput.requestFocus();

        // Add TextWatcher for real-time input processing
        codeInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not needed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Clean input to digits only
                String input = s.toString().replaceAll("[^0-9]", "");
                if (countryCodes.containsKey(input)) {
                    countryDisplay.setText(countryCodes.get(input));
                    emojiDisplay.setText(countryEmojis.get(input));
                } else {
                    countryDisplay.setText("");
                    emojiDisplay.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Not needed
            }
        });
    }

    private void loadCountryCodesFromCSV() {
        try {
            InputStream is = getAssets().open("countries.csv");
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            // Skip header line
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    // Remove quotes from CSV values
                    String code = parts[0].replaceAll("\"", "");
                    String name = parts[1].replaceAll("\"", "");
                    String emoji = parts.length > 2 ? parts[2].replaceAll("\"", "") : "";
                    countryCodes.put(code, name);
                    countryEmojis.put(code, emoji);
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Clear input and displays when app resumes
        codeInput.setText("");
        countryDisplay.setText("");
        emojiDisplay.setText("");
        codeInput.requestFocus();
    }
}
