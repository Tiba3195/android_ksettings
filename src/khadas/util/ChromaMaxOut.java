package com.khadas.util;

public class ChromaMaxOut implements Chroma {
    @Override
    public double get(Cam sourceColor) {
        // Intentionally high. Gamut mapping from impossible HCT to sRGB will ensure that
        // the maximum chroma is reached, even if lower than this constant.
        return Chroma.MAX_VALUE + 10.0;
    }
}
