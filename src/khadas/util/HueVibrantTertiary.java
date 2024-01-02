package com.khadas.util;
import java.util.ArrayList;
import java.util.List;
import android.util.Pair;
public class HueVibrantTertiary implements Hue {
    private final List<Pair<Integer, Integer>> hueToRotations;

    public HueVibrantTertiary() {
        hueToRotations = new ArrayList<>();
        hueToRotations.add(new Pair<>(0, 35));
        hueToRotations.add(new Pair<>(41, 30));
        hueToRotations.add(new Pair<>(61, 20));
        hueToRotations.add(new Pair<>(101, 25));
        hueToRotations.add(new Pair<>(131, 30));
        hueToRotations.add(new Pair<>(181, 35));
        hueToRotations.add(new Pair<>(251, 30));
        hueToRotations.add(new Pair<>(301, 25));
        hueToRotations.add(new Pair<>(360, 25));
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
