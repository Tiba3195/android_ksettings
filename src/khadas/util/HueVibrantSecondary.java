package com.khadas.util;
import java.util.ArrayList;
import java.util.List;
import android.util.Pair;
public class HueVibrantSecondary implements Hue {
    private final List<Pair<Integer, Integer>> hueToRotations;

    public HueVibrantSecondary() {
        hueToRotations = new ArrayList<>();
        hueToRotations.add(new Pair<>(0, 18));
        hueToRotations.add(new Pair<>(41, 15));
        hueToRotations.add(new Pair<>(61, 10));
        hueToRotations.add(new Pair<>(101, 12));
        hueToRotations.add(new Pair<>(131, 15));
        hueToRotations.add(new Pair<>(181, 18));
        hueToRotations.add(new Pair<>(251, 15));
        hueToRotations.add(new Pair<>(301, 12));
        hueToRotations.add(new Pair<>(360, 12));
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
