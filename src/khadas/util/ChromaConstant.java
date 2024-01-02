package com.khadas.util;

public class ChromaConstant implements Chroma {
    private final double chroma;

    public ChromaConstant(double chroma) {
        this.chroma = chroma;
    }

    @Override
    public double get(Cam sourceColor) {
        return chroma;
    }
}
