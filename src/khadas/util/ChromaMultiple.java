package com.khadas.util;

public class ChromaMultiple implements Chroma {
    private final double multiple;

    public ChromaMultiple(double multiple) {
        this.multiple = multiple;
    }

    @Override
    public double get(Cam sourceColor) {
        return sourceColor.getChroma() * multiple;
    }
}
