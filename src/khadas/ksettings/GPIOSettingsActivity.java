package com.khadas.ksettings;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.khadas.util.GPIOUtils;

import java.util.List;

public class GPIOSettingsActivity extends PreferenceActivity {

    private Dialog dialog;
    Button actionButton;
    Button cancelButton;
    EditText textInput;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.gpio_settings_control);

        // Export Pin Preference
        Preference exportPinPref = findPreference("export_pin");
        exportPinPref.setOnPreferenceClickListener(preference -> {
            showDialog(this,true);
            return true;
        });

        // Unexport Pin Preference
        Preference unexportPinPref = findPreference("unexport_pin");
        unexportPinPref.setOnPreferenceClickListener(preference -> {
            showDialog(this,false);
            return true;
        });

        // Set up RecyclerView within the custom preference
        Preference gpioPinsPreference = findPreference("gpio_pins");
        if (gpioPinsPreference != null) {
            gpioPinsPreference.setLayoutResource(R.layout.recycler_preference_layout);
        }
    }

    private void exportPin(int pin) {

        // Assuming GPIOControl.exportPin() is your method to handle the GPIO export
        GPIOControl.exportPin(pin);
        Toast.makeText(this, "Exported Pin: " + pin, Toast.LENGTH_SHORT).show();
    }

    private void unExportPin(int pin)  {

        // Assuming GPIOControl.unexportPin() is your method to handle the GPIO unexport
        GPIOControl.unexportPin(pin);
        Toast.makeText(this, "Unexported Pin: " + pin, Toast.LENGTH_SHORT).show();
    }

    public void showDialog(Context context,boolean exportMode) {
        this.dialog = new Dialog(context);
        this.dialog.setContentView(exportMode ? R.layout.export_popup_layout : R.layout.unexport_popup_layout);

        initializeDialogViews(exportMode,context);

        dialog.setOnDismissListener(dialogInterface -> {
            // Handle the dismissal
            onDialogDismissed();
        });

        dialog.show();
    }

    private void onDialogDismissed() {
    }

    private void initializeDialogViews(boolean exportMode,Context hostContext) {
        textInput = dialog.findViewById(R.id.text_popupinput);

        actionButton = dialog.findViewById(R.id.button_action);
        actionButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (textInput.getText().toString().isEmpty())
                {
                    Toast.makeText(hostContext, "Please enter a pin number", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if (exportMode)
                    {
                        exportPin(Integer.parseInt(textInput.getText().toString()));
                    } else
                    {
                        unExportPin(Integer.parseInt(textInput.getText().toString()));
                    }

                    if (dialog.isShowing())
                    {
                        dialog.dismiss();
                    }
                }
            }
        });
        cancelButton = dialog.findViewById(R.id.button_cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }});
    }


}