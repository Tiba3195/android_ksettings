package com.khadas.ksettings;

import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.khadas.util.GPIOUtils;

public class ThemePickerPreference extends Preference {

    public ThemePickerPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setLayoutResource(R.layout.theme_picker_layout); // Set your custom layout here
    }

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);

    }
}
