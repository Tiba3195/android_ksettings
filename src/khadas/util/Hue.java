package com.khadas.util;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import android.util.Pair;

public interface Hue {
    double get(Cam sourceColor);

    double getHueRotation(float sourceHue, List<Pair<Integer, Integer>> hueAndRotations);
}
