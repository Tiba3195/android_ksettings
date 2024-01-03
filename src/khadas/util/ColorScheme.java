package com.khadas.util;

import static com.khadas.util.ColorSchemeHelper.GOOGLE_BLUE;

import android.app.WallpaperColors;
import android.graphics.Color;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ColorScheme {
    public int getPrimarySeed() {
        return seed;
    }

    public Style getStyle() {
        return style;
    }

    private  List<Integer> SeedColors;
    @ColorInt
    private final int seed;
    private final boolean darkTheme;
    private final Style style;
    public TonalPalette accent1;
    public TonalPalette accent2;
    public TonalPalette accent3;
    public TonalPalette neutral1;
    public TonalPalette neutral2;

    public ColorScheme(List<Integer>  seeds, boolean darkTheme, Style style) {
        this.SeedColors = seeds;
        this.seed = seeds.get(0);
        this.darkTheme = darkTheme;
        this.style = style != null ? style : Style.TONAL_SPOT;

        int seedArgb = adjustSeed(seed, style);

        this.accent1 = new TonalPalette(style.coreSpec.getA1(), seedArgb);
        this.accent2 = new TonalPalette(style.coreSpec.getA2(), seedArgb);
        this.accent3 = new TonalPalette(style.coreSpec.getA3(), seedArgb);
        this.neutral1 = new TonalPalette(style.coreSpec.getN1(), seedArgb);
        this.neutral2 = new TonalPalette(style.coreSpec.getN2(), seedArgb);
    }

    public ColorScheme(@ColorInt int seed, boolean darkTheme) {
        this(new ArrayList<Integer>(){{ add(seed); }}, darkTheme, Style.TONAL_SPOT);
    }

    @RequiresApi(Build.VERSION_CODES.O_MR1)
    public ColorScheme(Map<Integer, Integer> allColors, List<Color> mainColors,
                       boolean darkTheme, Style style) {
        this(ColorSchemeHelper.getSeedColors(allColors, mainColors, style != Style.CONTENT),
                darkTheme, style);
    }

    @RequiresApi(Build.VERSION_CODES.O_MR1)
    public ColorScheme(WallpaperColors wallpaperColors, boolean darkTheme, Style style) {
        this(ColorSchemeHelper.getSeedColorsFromWallPaper(wallpaperColors, style != Style.CONTENT),
                darkTheme, style);
    }

    private int adjustSeed(int seed, Style style) {
        Cam proposedSeedCam = Cam.fromInt(seed);
        if (seed == Color.TRANSPARENT || (style != Style.CONTENT && proposedSeedCam.getChroma() < 5)) {
            return GOOGLE_BLUE;
        }
        return seed;
    }

    public List<TonalPalette> getAllHues() {
        List<TonalPalette> hues = new ArrayList<>();
        hues.add(accent1);
        hues.add(accent2);
        hues.add(accent3);
        hues.add(neutral1);
        hues.add(neutral2);
        return hues;
    }

    public List<Integer> getAllAccentColors() {
        List<Integer> allColors = new ArrayList<>();
        allColors.addAll(accent1.getAllShades());
        allColors.addAll(accent2.getAllShades());
        allColors.addAll(accent3.getAllShades());
        return allColors;
    }

    public List<Integer> getAllNeutralColors() {
        List<Integer> allColors = new ArrayList<>();
        allColors.addAll(neutral1.getAllShades());
        allColors.addAll(neutral2.getAllShades());
        return allColors;
    }

    public int getBackgroundColor() {
        return ColorUtils.setAlphaComponent(darkTheme ? neutral1.getS700() : neutral1.getS10(), 0xFF);
    }

    public int getAccentColor() {
        return ColorUtils.setAlphaComponent(darkTheme ? accent1.getS100() : accent1.getS500(), 0xFF);
    }

    public int getShadeCount() {
        return accent1.getAllShades().size();
    }

    public float getSeedTone() {
        return 1000f - CamUtils.lstarFromInt(seed) * 10f;
    }

    @Override
    public String toString() {
        return "ColorScheme {\n" +
                "  seed color: " + stringForColor(seed) + "\n" +
                "  style: " + style + "\n" +
                "  palettes: \n" +
                "  " + humanReadable("PRIMARY", accent1.getAllShades()) + "\n" +
                "  " + humanReadable("SECONDARY", accent2.getAllShades()) + "\n" +
                "  " + humanReadable("TERTIARY", accent3.getAllShades()) + "\n" +
                "  " + humanReadable("NEUTRAL", neutral1.getAllShades()) + "\n" +
                "  " + humanReadable("NEUTRAL VARIANT", neutral2.getAllShades()) + "\n" +
                "}";
    }

    private String humanReadable(String paletteName, List<Integer> colors) {
        StringBuilder sb = new StringBuilder(paletteName + "\n");
        for (Integer color : colors) {
            sb.append(stringForColor(color)).append("\n");
        }
        return sb.toString();
    }

    private String stringForColor(int color) {
        Cam hct = Cam.fromInt(color);
        String h = "H" + String.format("%4d", hct.getHue());
        String c = "C" + String.format("%4d", hct.getChroma());
        String t = "T" + String.format("%4d", CamUtils.lstarFromInt(color));
        String hex = String.format("#%06X", (0xFFFFFF & color));
        return h + c + t + " = " + hex;
    }
}
