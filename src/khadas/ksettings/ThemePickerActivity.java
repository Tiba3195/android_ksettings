package com.khadas.ksettings;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class ThemePickerActivity extends PreferenceActivity {

        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.canbus_settings_control);
        }
}
