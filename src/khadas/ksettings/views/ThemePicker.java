package com.khadas.ksettings.views;

import android.app.WallpaperColors;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
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
import java.util.Map;

public class ThemePicker extends LinearLayout {
    private Map<String, String> CurrentSettings;
    String[] themeStyles = Arrays.toString(ThemeChanger.ThemeStyle.values()).replaceAll("^.|.$", "").split(", ");
    private Spinner spinnerThemeStyle;

    private View seedColorView;
    private View primaryColorView;
    private View secondaryColorView;
    private View tertiaryColorView;
    private Button applyThemeButton;

    ThemeChanger.ThemeStyle selectedThemeStyle = ThemeChanger.ThemeStyle.TONAL_SPOT;
    Style selectedColorThemeStyle = Style.TONAL_SPOT;

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
        seedColorView= findViewById(R.id.seed_color);

        if(!isInEditMode())
        {
            applyThemeSettings(seedColorView);
        }

        primaryColorView = findViewById(R.id.primary_color);

        primaryColorView.setOnClickListener(v -> {
            Log.d("ThemePicker", "Show Color Picker: primaryColorView");
        });

        secondaryColorView = findViewById(R.id.secondary_color);

        secondaryColorView.setOnClickListener(v -> {
            Log.d("ThemePicker", "Show Color Picker: secondaryColorView");
        });

        tertiaryColorView = findViewById(R.id.tertiary_color);

        tertiaryColorView.setOnClickListener(v -> {
            Log.d("ThemePicker", "Show Color Picker: tertiaryColorView");
        });

        applyThemeButton = findViewById(R.id.button);

        Spinner spinnerThemeStyle = findViewById(R.id.spinnerThemeStyle);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.spinner_item, themeStyles);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinnerThemeStyle.setAdapter(adapter);

        spinnerThemeStyle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedThemeStyle = ThemeChanger.ThemeStyle.fromString((String) parent.getItemAtPosition(position));
                selectedColorThemeStyle = Style.fromString((String) parent.getItemAtPosition(position));
                CurrentSettings = ThemeChanger.getThemeCustomizationSettings();
                applyThemeSettings(seedColorView);
                // Handle the selected theme style
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });

        // Example usage (like setting listeners)
        applyThemeButton.setOnClickListener(v ->
        {
            int primaryColor =0;
            int secondaryColor =0;
            int tertiaryColor=0;

            // Assuming 'view' is your View object
            Drawable primaryColorData = primaryColorView.getBackground();
            if (primaryColorData instanceof ColorDrawable) {
                 primaryColor = ((ColorDrawable) primaryColorData).getColor();
                // 'color' is the background color of the view
            }

            Drawable secondaryColorData = secondaryColorView.getBackground();
            if (secondaryColorData instanceof ColorDrawable) {
                 secondaryColor = ((ColorDrawable) secondaryColorData).getColor();
                // 'color' is the background color of the view
            }

            Drawable tertiaryColorData = tertiaryColorView.getBackground();
            if (tertiaryColorData instanceof ColorDrawable) {
                tertiaryColor = ((ColorDrawable) tertiaryColorData).getColor();
                // 'color' is the background color of the view
            }

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O_MR1) {
                WallpaperColors wallpaperColors = new WallpaperColors(Color.valueOf(primaryColor), Color.valueOf(secondaryColor), Color.valueOf(tertiaryColor));
                ColorScheme colorScheme = new ColorScheme(wallpaperColors,true, Style.EXPRESSIVE);
                ThemeChanger.setThemeColor(colorScheme.getSeed(), ThemeChanger.ThemeStyle.EXPRESSIVE);
            }
        });


    }
    public void applyThemeSettings(View seedColorView) {
        Map<String, String> currentSettings = ThemeChanger.getThemeCustomizationSettings();

        // Extract the color string
        String colorString = currentSettings.get("android.theme.customization.system_palette");
        if (colorString != null) {
            // Add '#' if it's not present
            if (!colorString.startsWith("#")) {
                colorString = "#" + colorString;
            }

            try {
                int color = Color.parseColor(colorString);
                seedColorView.setBackgroundColor(color);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                // Handle invalid color string
            }
        }
    }
    // Additional methods to interact with the custom view (like setters, getters)
}

