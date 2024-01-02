package com.khadas.util;

import android.util.Pair;
import java.util.List;
public class HueAdd implements Hue {
    private final double amountDegrees;

    public HueAdd(double amountDegrees) {
        this.amountDegrees = amountDegrees;
    }

    @Override
    public double get(Cam sourceColor) {
        return ColorSchemeHelper.wrapDegreesDouble(sourceColor.getHue() + amountDegrees);
    }

    @Override
    public double getHueRotation(float sourceHue, List<Pair<Integer, Integer>> hueAndRotations) {
        // Implement this method if needed
        return sourceHue;
    }
}
