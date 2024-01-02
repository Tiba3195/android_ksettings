package com.khadas.util;

public class ChromaSource implements Chroma {
    @Override
    public double get(Cam sourceColor) {
        return sourceColor.getChroma();
    }
}
