package com.khadas.ksettings;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.khadas.util.GPIOUtils;

import java.util.List;

public class GPIOSettingsActivity extends PreferenceActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.gpio_settings_control);

        // Export Pin Preference
        Preference exportPinPref = findPreference("export_pin");
        exportPinPref.setOnPreferenceClickListener(preference -> {
            exportPin();
            return true;
        });

        // Unexport Pin Preference
        Preference unexportPinPref = findPreference("unexport_pin");
        unexportPinPref.setOnPreferenceClickListener(preference -> {
            unexportPin();
            return true;
        });

        // Set up RecyclerView within the custom preference
        Preference gpioPinsPreference = findPreference("gpio_pins");
        if (gpioPinsPreference != null) {
            gpioPinsPreference.setLayoutResource(R.layout.recycler_preference_layout);
        }


    }

    private void exportPin() {
        int pin = getPinNumber();
        // Assuming GPIOControl.exportPin() is your method to handle the GPIO export
        GPIOControl.exportPin(pin);
        Toast.makeText(this, "Exported Pin: " + pin, Toast.LENGTH_SHORT).show();
    }

    private void unexportPin() {
        int pin = getPinNumber();
        // Assuming GPIOControl.unexportPin() is your method to handle the GPIO unexport
        GPIOControl.unexportPin(pin);
        Toast.makeText(this, "Unexported Pin: " + pin, Toast.LENGTH_SHORT).show();
    }

    private int getPinNumber() {
        String pinString = PreferenceManager.getDefaultSharedPreferences(this)
                .getString("pin_number", "0");
        return Integer.parseInt(pinString);
    }
}