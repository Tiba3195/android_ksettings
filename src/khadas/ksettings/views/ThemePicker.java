package com.khadas.ksettings.views;

import static com.khadas.util.MathHelper.MapValueToRange;

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
import com.khadas.util.ColorSchemeHelper;
import com.khadas.util.Style;
import com.khadas.util.ThemeChanger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ThemePicker extends LinearLayout {

    private float[] hsv = {0, 1, 1};

    private ArrayList<SeedColorTileView> SeedColorTileViews = new ArrayList<>();

    private Map<String, String> CurrentSettings;
    String[] themeStyles = Arrays.toString(ThemeChanger.ThemeStyle.values()).replaceAll("^.|.$", "").split(", ");
    private Spinner spinnerThemeStyle;
    ColorScheme colorScheme;
    private ColorPickerView primaryColorView;
    private ColorPickerView secondaryColorView;
    private ColorPickerView tertiaryColorView;
    private Button applyThemeButton;
    private  ColorPaletteView colorPaletteView;

    private  ColorBlendedCircleView colorCircleView;

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
                applyThemeSettings(new ArrayList<String>(){{ add(colorString); }});
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
            applyThemeSettings(new ArrayList<String>(){{ add("#FF00000"); }});
        }

        seekBarHue.setProgress((int)MapValueToRange(0.1f,0.8f,0.0f,100.0f,0.5f));
        seekBarSaturation.setProgress((int)MapValueToRange(0.5f,2.5f,0.0f,100.0f,0.8f));
        seekBarBrightness.setProgress((int)MapValueToRange(4.0f,10.0f,0.0f,100.0f,6.9f));
    }

    private void setupSeekBars() {
        SeekBar.OnSeekBarChangeListener listener = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (seekBar == seekBarHue) {
                    colorCircleView.setPrimaryColorAmount(progress / 100f);
                } else if (seekBar == seekBarSaturation) {
                    colorCircleView.setSecondaryColorAmount(progress / 100f);
                } else if (seekBar == seekBarBrightness) {
                    colorCircleView.setTertiaryColorAmount(progress / 100f);
                }

                ArrayList<String> ColorStrings = new ArrayList<>();
                for (int seedColor : getSeeds() )
                {
                    ColorStrings.add(String.format("#%06X", (0xFFFFFF & seedColor)));
                }

                applyThemeSettings(ColorStrings);
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

        seekBarHue = findViewById(R.id.seekBarHue);
        seekBarSaturation = findViewById(R.id.seekBarSaturation);
        seekBarBrightness = findViewById(R.id.seekBarBrightness);
        colorPaletteView = findViewById(R.id.colorPaletteView);
        colorCircleView = findViewById(R.id.colorCircleView);

        SeedColorTileViews.add(findViewById(R.id.seed_color_tile1));
        SeedColorTileViews.get(0).setOnColorSelectedListener(color -> {
            Log.d("ThemePicker", "Seed Color 1 Selected: " + color);
            primarySeedColor = color;
            colorScheme = new ColorScheme(Collections.singletonList(primarySeedColor), true, selectedColorThemeStyle);
            colorPaletteView.setColorScheme(colorScheme);
        });

        SeedColorTileViews.add(findViewById(R.id.seed_color_tile2));
        SeedColorTileViews.get(1).setOnColorSelectedListener(color -> {
            Log.d("ThemePicker", "Seed Color 2 Selected: " + color);
            primarySeedColor = color;
            colorScheme = new ColorScheme(Collections.singletonList(primarySeedColor), true, selectedColorThemeStyle);
            colorPaletteView.setColorScheme(colorScheme);
        });

        SeedColorTileViews.add(findViewById(R.id.seed_color_tile3));
        SeedColorTileViews.get(2).setOnColorSelectedListener(color -> {
            Log.d("ThemePicker", "Seed Color 3 Selected: " + color);
            primarySeedColor = color;
            colorScheme = new ColorScheme(Collections.singletonList(primarySeedColor), true, selectedColorThemeStyle);
            colorPaletteView.setColorScheme(colorScheme);
        });

        SeedColorTileViews.add(findViewById(R.id.seed_color_tile4));
        SeedColorTileViews.get(3).setOnColorSelectedListener(color -> {
            Log.d("ThemePicker", "Seed Color 4 Selected: " + color);
            primarySeedColor = color;
            colorScheme = new ColorScheme(Collections.singletonList(primarySeedColor), true, selectedColorThemeStyle);
            colorPaletteView.setColorScheme(colorScheme);
        });

        setupSeekBars();

        primaryColorView = findViewById(R.id.primary_color);
        primaryColorView.setOnColorSelectedListener(color -> {
            Log.d("ThemePicker", "Primary Color Selected: " + color);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {

                ArrayList<String> ColorStrings = new ArrayList<>();
                for (int seedColor : getSeeds() )
                {
                    ColorStrings.add(String.format("#%06X", (0xFFFFFF & seedColor)));
                }

                applyThemeSettings(ColorStrings);
            }

        });

        primaryColorView.setOnClickListener(v -> {
            Log.d("ThemePicker", "Show Color Picker: primaryColorView");
        });

        secondaryColorView = findViewById(R.id.secondary_color);
        secondaryColorView.setOnColorSelectedListener(color -> {
            Log.d("ThemePicker", "Primary Color Selected: " + color);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {

                ArrayList<String> ColorStrings = new ArrayList<>();
                for (int seedColor : getSeeds() )
                {
                    ColorStrings.add(String.format("#%06X", (0xFFFFFF & seedColor)));
                }

                applyThemeSettings(ColorStrings);
            }

        });
        secondaryColorView.setOnClickListener(v -> {
            Log.d("ThemePicker", "Show Color Picker: secondaryColorView");
        });

        tertiaryColorView = findViewById(R.id.tertiary_color);
        tertiaryColorView.setOnColorSelectedListener(color -> {
            Log.d("ThemePicker", "Primary Color Selected: " + color);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {

                ArrayList<String> ColorStrings = new ArrayList<>();
                for (int seedColor : getSeeds() )
                {
                    ColorStrings.add(String.format("#%06X", (0xFFFFFF & seedColor)));
                }

                applyThemeSettings(ColorStrings);
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
                ThemeChanger.setThemeColor(primarySeedColor, selectedThemeStyle);
            }
        });
    }

    private int primarySeedColor=0;

    @RequiresApi(api = Build.VERSION_CODES.O_MR1)
    private List<Integer> getSeeds() {
        Log.d("ThemePicker", "<=======================================>");
        Log.d("ThemePicker", "getSeed called");

        int primaryColor = primaryColorView.getCurrentColor();
        int secondaryColor = secondaryColorView.getCurrentColor();
        int tertiaryColor = tertiaryColorView.getCurrentColor();
        colorCircleView.setColors(primaryColor, secondaryColor, tertiaryColor);
        Log.d("ThemePicker", "Primary Color: " + primaryColor + ", Secondary Color: " + secondaryColor + ", Tertiary Color: " + tertiaryColor);

        WallpaperColors wallpaperColors = WallpaperColors.fromBitmap(colorCircleView.getBitmap());

        List<Integer>  seeds =  ColorSchemeHelper.getSeedColorsFromWallPaper(wallpaperColors,true);

        colorPaletteView.invalidate();
        Log.d("ThemePicker", "Generated Seed: " + seeds);
        Log.d("ThemePicker", "<=======================================>");
        return seeds;
    }


    public void applyThemeSettings(ArrayList<String> colorStrings) {
        Log.d("ThemePicker", "<=======================================>");
        Log.d("ThemePicker", "applyThemeSettings called with colorString: " + colorStrings);

        for (String colorString : colorStrings) {

            SeedColorTileView seedColorTileView = SeedColorTileViews.get(colorStrings.indexOf(colorString));

            if (colorStrings != null) {
                // Add '#' if it's not present
                if (!colorString.startsWith("#")) {
                    colorString = "#" + colorString;
                    Log.d("ThemePicker", "Added # to colorString: " + colorString);
                }

                try {
                    seedColorTileView.setSeedColor(colorString);
                    seedColorTileView.invalidate();

                    Log.d("ThemePicker", "Applied color to seedColorView: " + colorString);
                } catch (IllegalArgumentException e) {
                    Log.e("ThemePicker", "Invalid color string: " + colorString, e);
                    // Handle invalid color string
                }
            } else {
                Log.d("ThemePicker", "Received null colorString");
            }
        }


        Log.d("ThemePicker", "<=======================================>");
    }
    // Additional methods to interact with the custom view (like setters, getters)
}

