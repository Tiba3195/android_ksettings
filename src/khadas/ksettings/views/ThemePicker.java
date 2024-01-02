package com.khadas.ksettings.views;

import android.app.WallpaperColors;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Button;
import android.view.View;

import com.khadas.ksettings.R;
import com.khadas.util.ColorScheme;
import com.khadas.util.Style;
import com.khadas.util.ThemeChanger;
import com.khadas.util.ColorSchemeHelper;

import java.util.Arrays;

public class ThemePicker extends LinearLayout {

    String[] themeStyles = Arrays.toString(ThemeChanger.ThemeStyle.values()).replaceAll("^.|.$", "").split(", ");
    private Spinner spinnerThemeStyle;
    private View primaryColor;
    private View secondaryColor;
    private View tertiaryColor;
    private Button applyThemeButton;

    public ThemePicker(Context context) {
        super(context);
        init(context);
    }

    public ThemePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ThemePicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        // Inflate the layout
        LayoutInflater.from(context).inflate(R.layout.theme_picker_layout, this, true);

        // Initialize components
        spinnerThemeStyle = findViewById(R.id.spinnerThemeStyle);
        primaryColor = findViewById(R.id.primary_color);
        secondaryColor = findViewById(R.id.secondary_color);
        tertiaryColor = findViewById(R.id.tertiary_color);
        applyThemeButton = findViewById(R.id.button);

        // Example usage (like setting listeners)
        applyThemeButton.setOnClickListener(v ->
        {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O_MR1) {
                WallpaperColors wallpaperColors = new WallpaperColors(Color.valueOf(Color.RED), Color.valueOf(Color.BLUE), Color.valueOf(Color.GREEN));
                ColorScheme colorScheme = new ColorScheme(wallpaperColors,true, Style.EXPRESSIVE);
                ThemeChanger.setThemeColor(colorScheme.getSeed(), ThemeChanger.ThemeStyle.EXPRESSIVE);
            }
        });

        Spinner spinnerThemeStyle = findViewById(R.id.spinnerThemeStyle);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.spinner_item, themeStyles);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinnerThemeStyle.setAdapter(adapter);

        spinnerThemeStyle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedThemeStyle = (String) parent.getItemAtPosition(position);
                // Handle the selected theme style
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });
    }

    // Additional methods to interact with the custom view (like setters, getters)
}

