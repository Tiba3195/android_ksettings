package com.khadas.util;

import java.util.ArrayList;
import java.util.List;
import android.util.Pair;
public class HueExpressiveTertiary implements Hue {
    private final List<Pair<Integer, Integer>> hueToRotations;

    public HueExpressiveTertiary() {
        hueToRotations = new ArrayList<>();
        hueToRotations.add(new Pair<>(0, 120));
        hueToRotations.add(new Pair<>(21, 120));
        hueToRotations.add(new Pair<>(51, 20));
        hueToRotations.add(new Pair<>(121, 45));
        hueToRotations.add(new Pair<>(151, 20));
        hueToRotations.add(new Pair<>(191, 15));
        hueToRotations.add(new Pair<>(271, 20));
        hueToRotations.add(new Pair<>(321, 120));
        hueToRotations.add(new Pair<>(360, 120));
    }

    @Override
    public double get(Cam sourceColor) {
        return getHueRotation(sourceColor.getHue(), hueToRotations);
    }

    @Override
    public double getHueRotation(float sourceHue, List<Pair<Integer, Integer>> hueAndRotations) {
        // Implement this method if needed
        return sourceHue;
    }
}