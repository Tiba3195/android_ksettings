package com.khadas.util;

import java.util.ArrayList;
import java.util.List;
import android.util.Pair;
public class HueExpressiveSecondary implements Hue {
    private final List<Pair<Integer, Integer>> hueToRotations;

    public HueExpressiveSecondary() {
        hueToRotations = new ArrayList<>();
        hueToRotations.add(new Pair<>(0, 45));
        hueToRotations.add(new Pair<>(21, 95));
        hueToRotations.add(new Pair<>(51, 45));
        hueToRotations.add(new Pair<>(121, 20));
        hueToRotations.add(new Pair<>(151, 45));
        hueToRotations.add(new Pair<>(191, 90));
        hueToRotations.add(new Pair<>(271, 45));
        hueToRotations.add(new Pair<>(321, 45));
        hueToRotations.add(new Pair<>(360, 45));
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