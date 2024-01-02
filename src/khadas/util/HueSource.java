package com.khadas.util;

import android.util.Pair;

import java.util.List;

public class HueSource implements Hue {
    @Override
    public double get(Cam sourceColor) {
        return sourceColor.getHue();
    }

    @Override
    public double getHueRotation(float sourceHue, List<Pair<Integer, Integer>> hueAndRotations) {
        float sanitizedSourceHue = (sourceHue < 0 || sourceHue >= 360) ? 0 : sourceHue;
        for (int i = 0; i < hueAndRotations.size() - 1; i++) {
            float thisHue = hueAndRotations.get(i).first;
            float nextHue = hueAndRotations.get(i + 1).first;
            if (thisHue <= sanitizedSourceHue && sanitizedSourceHue < nextHue) {
                return ColorSchemeHelper.wrapDegreesDouble(sanitizedSourceHue + hueAndRotations.get(i).second);
            }
        }
        return sourceHue;
    }
}
