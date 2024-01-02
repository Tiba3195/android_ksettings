package com.khadas.ksettings.views;

import android.app.WallpaperColors;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Button;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.khadas.ksettings.R;
import com.khadas.util.ColorScheme;
import com.khadas.util.Style;
import com.khadas.util.ThemeChanger;

import java.util.Arrays;
import java.util.Map;

public class ThemePicker extends LinearLayout {

    private float[] hsv = {0, 1, 1};

    private Map<String, String> CurrentSettings;
    String[] themeStyles = Arrays.toString(ThemeChanger.ThemeStyle.values()).replaceAll("^.|.$", "").split(", ");
    private Spinner spinnerThemeStyle;
    ColorScheme colorScheme;
    private TextView seedColorValueView;
    private View seedColorView;
    private ColorPickerView primaryColorView;
    private ColorPickerView secondaryColorView;
    private ColorPickerView tertiaryColorView;
    private Button applyThemeButton;
    private  ColorPaletteView colorPaletteView;

    private SeekBar seekBarHue, seekBarSaturation, seekBarBrightness;
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

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        Log.d("ThemePicker", "onFinishInflate called");

        if(!isInEditMode()) {
            Map<String, String> currentSettings = ThemeChanger.getThemeCustomizationSettings();
            Log.d("ThemePicker", "Current Settings: " + currentSettings);

            String colorString = currentSettings.get("android.theme.customization.system_palette");
            String styleString = currentSettings.get("android.theme.customization.theme_style");

            Log.d("ThemePicker", "Color String: " + colorString + ", Style String: " + styleString);

            if (colorString != null && colorString.matches("#[0-9a-fA-F]{6}")) {
                applyThemeSettings(colorString);
            } else {
                Log.d("ThemePicker", "Invalid or null color string");
            }

            for (int i = 0; i < themeStyles.length; i++) {
                if (themeStyles[i].equals(styleString)) {
                    spinnerThemeStyle.setSelection(i);
                    Log.d("ThemePicker", "Spinner selection set to index: " + i);
                    break;
                }
            }
        } else {
            Log.d("ThemePicker", "In edit mode, skipping onFinishInflate setup");
        }
    }

    private void setupSeekBars() {
        SeekBar.OnSeekBarChangeListener listener = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (seekBar == seekBarHue) {
                    hsv[0] = progress;
                } else if (seekBar == seekBarSaturation) {
                    hsv[1] = progress / 100f;
                } else if (seekBar == seekBarBrightness) {
                    hsv[2] = progress / 100f;
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        };

        seekBarHue.setOnSeekBarChangeListener(listener);
        seekBarSaturation.setOnSeekBarChangeListener(listener);
        seekBarBrightness.setOnSeekBarChangeListener(listener);
    }


    private void init(Context context) {
        // Inflate the layout
        LayoutInflater.from(context).inflate(R.layout.theme_picker_layout, this, true);

        // Initialize components
        spinnerThemeStyle = findViewById(R.id.spinnerThemeStyle);
        seedColorView= findViewById(R.id.seed_color);
        seedColorValueView = findViewById(R.id.text_seed_color_value);
        seekBarHue = findViewById(R.id.seekBarHue);
        seekBarSaturation = findViewById(R.id.seekBarSaturation);
        seekBarBrightness = findViewById(R.id.seekBarBrightness);
        colorPaletteView = findViewById(R.id.colorPaletteView);

        // Set SeekBar initial values
        seekBarHue.setProgress((int) hsv[0]);
        seekBarSaturation.setProgress((int) (hsv[1] * 100));
        seekBarBrightness.setProgress((int) (hsv[2] * 100));

        setupSeekBars();

        primaryColorView = findViewById(R.id.primary_color);
        primaryColorView.setOnColorSelectedListener(color -> {
            Log.d("ThemePicker", "Primary Color Selected: " + color);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {

                String colorHex = String.format("#%06X", (0xFFFFFF & getSeed()));
                seedColorValueView.setText( colorHex);
                applyThemeSettings(colorHex);
            }

        });

        primaryColorView.setOnClickListener(v -> {
            Log.d("ThemePicker", "Show Color Picker: primaryColorView");
        });

        secondaryColorView = findViewById(R.id.secondary_color);
        secondaryColorView.setOnColorSelectedListener(color -> {
            Log.d("ThemePicker", "Primary Color Selected: " + color);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {

                String colorHex = String.format("#%06X", (0xFFFFFF & getSeed()));
                seedColorValueView.setText( colorHex);
                applyThemeSettings(colorHex);
            }

        });
        secondaryColorView.setOnClickListener(v -> {
            Log.d("ThemePicker", "Show Color Picker: secondaryColorView");
        });

        tertiaryColorView = findViewById(R.id.tertiary_color);
        tertiaryColorView.setOnColorSelectedListener(color -> {
            Log.d("ThemePicker", "Primary Color Selected: " + color);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {

                String colorHex = String.format("#%06X", (0xFFFFFF & getSeed()));
                seedColorValueView.setText( colorHex);
                applyThemeSettings(colorHex);
            }

        });
        tertiaryColorView.setOnClickListener(v -> {
            Log.d("ThemePicker", "Show Color Picker: tertiaryColorView");
        });

        applyThemeButton = findViewById(R.id.button_apply_seed);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.spinner_item, themeStyles);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinnerThemeStyle.setAdapter(adapter);

        spinnerThemeStyle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedThemeStyle = ThemeChanger.ThemeStyle.fromString((String) parent.getItemAtPosition(position));
                selectedColorThemeStyle = Style.fromString((String) parent.getItemAtPosition(position));
                CurrentSettings = ThemeChanger.getThemeCustomizationSettings();

                spinnerThemeStyle.setSelection(position);
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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
                ThemeChanger.setThemeColor(getSeed(), selectedThemeStyle);

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O_MR1)
    private int getSeed() {
        Log.d("ThemePicker", "<=======================================>");
        Log.d("ThemePicker", "getSeed called");

        int primaryColor = primaryColorView.getCurrentColor();
        int secondaryColor = secondaryColorView.getCurrentColor();
        int tertiaryColor = tertiaryColorView.getCurrentColor();

        Log.d("ThemePicker", "Primary Color: " + primaryColor + ", Secondary Color: " + secondaryColor + ", Tertiary Color: " + tertiaryColor);

        WallpaperColors wallpaperColors = new WallpaperColors(Color.valueOf(primaryColor), Color.valueOf(secondaryColor), Color.valueOf(tertiaryColor));
        colorScheme = new ColorScheme(wallpaperColors, true, selectedColorThemeStyle);

        int seed = colorScheme.getSeed();
        colorPaletteView.setColorScheme(colorScheme);
        colorPaletteView.invalidate();
        Log.d("ThemePicker", "Generated Seed: " + seed);
        Log.d("ThemePicker", "<=======================================>");
        return seed;
    }


    public void applyThemeSettings(String colorString) {
        Log.d("ThemePicker", "<=======================================>");
        Log.d("ThemePicker", "applyThemeSettings called with colorString: " + colorString);

        if (colorString != null) {
            // Add '#' if it's not present
            if (!colorString.startsWith("#")) {
                colorString = "#" + colorString;
                Log.d("ThemePicker", "Added # to colorString: " + colorString);
            }

            seedColorValueView.setText(colorString);
            Log.d("ThemePicker", "Set seed color value text: " + colorString);

            try {
                int color = Color.parseColor(colorString);
                seedColorView.setBackgroundColor(color);
                seedColorView.invalidate();
                Log.d("ThemePicker", "Applied color to seedColorView: " + colorString);
            } catch (IllegalArgumentException e) {
                Log.e("ThemePicker", "Invalid color string: " + colorString, e);
                // Handle invalid color string
            }
        } else {
            Log.d("ThemePicker", "Received null colorString");
        }
        Log.d("ThemePicker", "<=======================================>");
    }
    // Additional methods to interact with the custom view (like setters, getters)
}

