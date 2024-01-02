package com.khadas.util;

public interface Chroma {
    double get(Cam sourceColor);

    double MAX_VALUE = 120.0;
    double MIN_VALUE = 0.0;
}
