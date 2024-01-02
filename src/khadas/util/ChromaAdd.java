package com.khadas.util;

public class ChromaAdd implements Chroma {
    private final double amount;

    public ChromaAdd(double amount) {
        this.amount = amount;
    }

    @Override
    public double get(Cam sourceColor) {
        return sourceColor.getChroma() + amount;
    }
}
