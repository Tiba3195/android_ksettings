package com.khadas.util;

import android.annotation.SuppressLint;
import android.app.WallpaperColors;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ColorSchemeHelper {
    static float ACCENT1_CHROMA = 48.0f;
    static int GOOGLE_BLUE = 0xFF1b6ef3;
    static float MIN_CHROMA = 5;

    @RequiresApi(Build.VERSION_CODES.O_MR1)
    @ColorInt
    public static int getSeedColor(Map<Integer, Integer> allColors, List<Color> mainColors, boolean filter) {
        return getSeedColors(allColors, mainColors, filter).get(0);
    }

    @RequiresApi(Build.VERSION_CODES.O_MR1)
    @ColorInt
    public static int getSeedColorFromWallPaper(WallpaperColors wallpaperColors, boolean filter) {
        return getSeedColorsFromWallPaper(wallpaperColors, filter).get(0);
    }

    @RequiresApi(Build.VERSION_CODES.O_MR1)
    public static List<Integer> getSeedColorsFromWallPaper(WallpaperColors wallpaperColors, boolean filter) {
        try {
            @SuppressLint("BlockedPrivateApi") Method getAllColorsMethod = WallpaperColors.class.getDeclaredMethod("getAllColors");
            getAllColorsMethod.setAccessible(true);
            Map<Integer, Integer> allColors = (Map<Integer, Integer>) getAllColorsMethod.invoke(wallpaperColors);

            @SuppressLint("SoonBlockedPrivateApi") Method getMainColorsMethod = WallpaperColors.class.getDeclaredMethod("getMainColors");
            getMainColorsMethod.setAccessible(true);
            List<Color> mainColors = (List<Color>) getMainColorsMethod.invoke(wallpaperColors);

            return getSeedColors(allColors, mainColors, filter);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @RequiresApi(Build.VERSION_CODES.O_MR1)
    public static List<Integer> getSeedColors(Map<Integer, Integer> allColors, List<Color> mainColors, boolean filter) {
        Log.d("SeedColor", "Starting getSeedColors");

        double totalPopulation = allColors.values().stream().mapToDouble(Integer::doubleValue).sum();
        boolean totalPopulationMeaningless = totalPopulation == 0.0;
        Log.d("SeedColor", "Total Population: " + totalPopulation);

        if (totalPopulationMeaningless) {
            Log.d("SeedColor", "Total population is zero, using main colors");
            List<Integer> distinctColors = mainColors.stream()
                    .map(Color::toArgb)
                    .distinct()
                    .filter(color -> !filter || Cam.fromInt(color).getChroma() >= MIN_CHROMA)
                    .collect(Collectors.toList());

            Log.d("SeedColor", "Distinct Colors: " + distinctColors);
            return distinctColors.isEmpty() ? Collections.singletonList(GOOGLE_BLUE) : distinctColors;
        }

        Map<Integer, Double> intToProportion = allColors.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue() / totalPopulation));
        Log.d("SeedColor", "Int to Proportion: " + intToProportion);

        Map<Integer, Cam> intToCam = allColors.keySet().stream()
                .collect(Collectors.toMap(key -> key, key -> Cam.fromInt(key)));
        Log.d("SeedColor", "Int to Cam: " + intToCam);

        List<Double> hueProportions = huePopulations(intToCam, intToProportion, filter);
        Log.d("SeedColor", "Hue Proportions: " + hueProportions);

        Map<Integer, Double> intToHueProportion = allColors.keySet().stream()
                .collect(Collectors.toMap(key -> key, key -> {
                    Cam cam = intToCam.get(key);
                    int hue = Math.round(cam.getHue());
                    double proportion = 0.0;
                    for (int i = hue - 15; i <= hue + 15; i++) {
                        proportion += hueProportions.get(wrapDegrees(i));
                    }
                    return proportion;
                }));
        Log.d("SeedColor", "Int to Hue Proportion: " + intToHueProportion);

        Map<Integer, Cam> filteredIntToCam = filter ? intToCam.entrySet().stream()
                .filter(entry -> {
                    Cam cam = entry.getValue();
                    double proportion = intToHueProportion.get(entry.getKey());
                    return cam.getChroma() >= MIN_CHROMA && (totalPopulationMeaningless || proportion > 0.01);
                }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)) : intToCam;
        Log.d("SeedColor", "Filtered Int to Cam: " + filteredIntToCam);

        List<Map.Entry<Integer, Cam>> intToScore = new ArrayList<>(filteredIntToCam.entrySet());
        intToScore.sort((e1, e2) -> Double.compare(score(e2.getValue(), intToHueProportion.get(e2.getKey())),
                score(e1.getValue(), intToHueProportion.get(e1.getKey()))));
        Log.d("SeedColor", "Int to Score (sorted): " + intToScore);

        List<Integer> seeds = new ArrayList<>();
        for (int i = 90; i >= 15; i--) {
            seeds.clear();
            Log.d("SeedColor", "Iteration with hue difference threshold: " + i);

            for (Map.Entry<Integer, Cam> entry : intToScore) {
                int color = entry.getKey();
                Log.d("SeedColor", "Evaluating color: " + color);

                int finalI = i;
                boolean existingSeedNearby = seeds.stream().anyMatch(seed -> {
                    double hueA = intToCam.get(color).getHue();
                    double hueB = intToCam.get(seed).getHue();
                    boolean isNearby = hueDiff(hueA, hueB) < finalI;
                    Log.d("SeedColor", "Comparing color " + color + " with seed " + seed + ": HueA = " + hueA + ", HueB = " + hueB + ", Hue difference = " + hueDiff(hueA, hueB) + ", isNearby: " + isNearby);
                    return isNearby;
                });

                if (!existingSeedNearby) {
                    seeds.add(color);
                    Log.d("SeedColor", "Added color " + color + " to seeds");
                    if (seeds.size() >= 4) {
                        Log.d("SeedColor", "Reached 4 seeds, breaking loop");
                        break;
                    }
                }
            }

            if (!seeds.isEmpty()) {
                Log.d("SeedColor", "Seeds identified, breaking main loop");
                break;
            }
        }

        Log.d("SeedColor", "Final seeds: " + seeds);
        return seeds.isEmpty() ? Collections.singletonList(GOOGLE_BLUE) : seeds;
    }

    private static int wrapDegrees(int degrees) {
        return (degrees + 360) % 360;
    }

    public static double wrapDegreesDouble(double degrees) {
        return (degrees + 360) % 360;
    }

    private static float hueDiff(double a, double b) {
        return (float) (180.0 - Math.abs(Math.abs(a - b) - 180.0));
    }

    private static String stringForColor(int color) {
        Cam hct = Cam.fromInt(color);
        String h = "H" + String.format("%4d", Math.round(hct.getHue()));
        String c = "C" + String.format("%4d", Math.round(hct.getChroma()));
        String t = "T" + String.format("%4d", Math.round(CamUtils.lstarFromInt(color)));
        String hex = String.format("#%06X", (0xFFFFFF & color));
        return h + c + t + " = " + hex;
    }

    private static String humanReadable(String paletteName, List<Integer> colors) {
        StringBuilder sb = new StringBuilder(paletteName).append("\n");
        colors.forEach(color -> sb.append(stringForColor(color)).append("\n"));
        return sb.toString();
    }

    private static double score(Cam cam, double proportion) {
        double proportionScore = 0.7 * 100.0 * proportion;
        double chromaScore = (cam.getChroma() < ACCENT1_CHROMA) ? 0.1 * (cam.getChroma() - ACCENT1_CHROMA)
                : 0.3 * (cam.getChroma() - ACCENT1_CHROMA);
        return chromaScore + proportionScore;
    }

    private static List<Double> huePopulations(Map<Integer, Cam> camByColor, Map<Integer, Double> populationByColor, boolean filter) {
        List<Double> huePopulation = new ArrayList<>(Collections.nCopies(360, 0.0));
        populationByColor.forEach((color, population) -> {
            Cam cam = camByColor.get(color);
            int hue = Math.round(cam.getHue()) % 360;
            if (!filter || cam.getChroma() > MIN_CHROMA) {
                huePopulation.set(hue, huePopulation.get(hue) + population);
            }
        });
        return huePopulation;
    }
}
