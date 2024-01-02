package com.khadas.util;

import java.util.List;
import android.util.Pair;

public class HueSubtract implements Hue {
    private final double amountDegrees;

    public HueSubtract(double amountDegrees) {
        this.amountDegrees = amountDegrees;
    }

    @Override
    public double get(Cam sourceColor) {
        return ColorSchemeHelper.wrapDegreesDouble(sourceColor.getHue() - amountDegrees);
    }

    @Override
    public double getHueRotation(float sourceHue, List<Pair<Integer, Integer>> hueAndRotations) {
        // Implement this method if needed
        return sourceHue;
    }
}
