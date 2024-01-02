package com.khadas.util;

import android.util.Pair;

import java.util.List;

public class HueHelper {
    public static double getHueRotation(float sourceHue, List<Pair<Integer, Integer>> hueAndRotations) {
        float sanitizedSourceHue = (sourceHue < 0 || sourceHue >= 360) ? 0 : sourceHue;
        for (int i = 0; i < hueAndRotations.size() - 1; i++) {
            float thisHue = hueAndRotations.get(i).first;
            float nextHue = hueAndRotations.get(i + 1).first;
            if (thisHue <= sanitizedSourceHue && sanitizedSourceHue < nextHue) {
                return ColorSchemeHelper.wrapDegreesDouble(sanitizedSourceHue + hueAndRotations.get(i).second);
            }
        }

        // If this statement executes, something is wrong, there should have been a rotation
        // found using the arrays.
        return sourceHue;
    }

    public static double getHueRotation(float sourceHue, int[] hueArray, int[] rotationArray) {
        for (int i = 0; i < hueArray.length - 1; i++) {
            float thisHue = hueArray[i];
            float nextHue = hueArray[i + 1];
            if (thisHue <= sourceHue && sourceHue < nextHue) {
                return ColorSchemeHelper.wrapDegreesDouble(sourceHue + rotationArray[i]);
            }
        }

        // If this statement executes, something is wrong, there should have been a rotation
        // found using the arrays.
        return sourceHue;
    }

    public static double getHueFromCam(Cam sourceColor) {
        return sourceColor.getHue();
    }

    public static double getHueAdd(float sourceHue, double amountDegrees) {
        return ColorSchemeHelper.wrapDegreesDouble(sourceHue + amountDegrees);
    }

    public static double getHueSubtract(float sourceHue, double amountDegrees) {
        return ColorSchemeHelper.wrapDegreesDouble(sourceHue - amountDegrees);
    }

    public static double getHueVibrantSecondary(float sourceHue) {
        int[] hueArray = {0, 41, 61, 101, 131, 181, 251, 301, 360};
        int[] rotationArray = {18, 15, 10, 12, 15, 18, 15, 12, 12};
        return getHueRotation(sourceHue, hueArray, rotationArray);
    }

    public static double getHueVibrantTertiary(float sourceHue) {
        int[] hueArray = {0, 41, 61, 101, 131, 181, 251, 301, 360};
        int[] rotationArray = {35, 30, 20, 25, 30, 35, 30, 25, 25};
        return getHueRotation(sourceHue, hueArray, rotationArray);
    }

    public static double getHueExpressiveSecondary(float sourceHue) {
        int[] hueArray = {0, 21, 51, 121, 151, 191, 271, 321, 360};
        int[] rotationArray = {45, 95, 45, 20, 45, 90, 45, 45, 45};
        return getHueRotation(sourceHue, hueArray, rotationArray);
    }

    public static double getHueExpressiveTertiary(float sourceHue) {
        int[] hueArray = {0, 21, 51, 121, 151, 191, 271, 321, 360};
        int[] rotationArray = {120, 120, 20, 45, 20, 15, 20, 120, 120};
        return getHueRotation(sourceHue, hueArray, rotationArray);
    }
}
