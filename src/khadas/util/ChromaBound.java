package com.khadas.util;
public class ChromaBound implements Chroma {
    private final Chroma baseChroma;
    private final double minVal;
    private final double maxVal;

    public ChromaBound(Chroma baseChroma, double minVal, double maxVal) {
        this.baseChroma = baseChroma;
        this.minVal = minVal;
        this.maxVal = maxVal;
    }

    @Override
    public double get(Cam sourceColor) {
        double result = baseChroma.get(sourceColor);
        return Math.min(Math.max(result, minVal), maxVal);
    }
}