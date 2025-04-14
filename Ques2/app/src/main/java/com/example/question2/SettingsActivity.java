package com.example.question2;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class SettingsActivity extends AppCompatActivity {

    private Switch themeSwitch;
    private static final String PREFS_NAME = "app_prefs";
    private static final String THEME_KEY = "dark_theme";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Load saved theme before setting content view
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean isDarkMode = prefs.getBoolean(THEME_KEY, false);
        AppCompatDelegate.setDefaultNightMode(isDarkMode ?
                AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Show back button in action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Initialize switch and sync with current theme
        themeSwitch = findViewById(R.id.themeSwitch);
        themeSwitch.setChecked(isDarkMode);

        // Handle switch toggle
        themeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean(THEME_KEY, isChecked);
                editor.apply();

                // Apply new theme
                AppCompatDelegate.setDefaultNightMode(isChecked ?
                        AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
                recreate(); // Recreate the activity to apply the theme
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish(); // Go back to MainActivity
        return true;
    }
}
