package com.khadas.util;

import java.util.ArrayList;
import java.util.List;

public class TonalSpec {

    public Hue getHue() {
        return hue;
    }

    public Chroma getChroma() {
        return chroma;
    }

    private final Hue hue;
    private final Chroma chroma;

    public TonalSpec(Hue hue, Chroma chroma) {
        this.hue = hue;
        this.chroma = chroma;
    }



    public List<Integer> shades(Cam sourceColor) {
        double hueValue = hue.get(sourceColor);
        double chromaValue = chroma.get(sourceColor);

        List<Integer> shades = new ArrayList<>();
        for (int shade : Shades.of((float) hueValue, (float) chromaValue)) {
            shades.add(Math.round(shade));
        }

        return shades;
    }

    public int getAtTone(Cam sourceColor, float tone) {
        double hueValue = hue.get(sourceColor);
        double chromaValue = chroma.get(sourceColor);
        return ColorUtils.CAMToColor((float) hueValue, (float) chromaValue, (1000f - tone) / 10f);
    }
}
