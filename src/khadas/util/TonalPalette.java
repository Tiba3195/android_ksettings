package com.khadas.util;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class TonalPalette {
    private final TonalSpec spec;
    private final Cam seedCam;

    public List<Integer> getAllShades() {
        return allShades;
    }

    private final List<Integer> allShades;
    private final Map<Integer, Integer> allShadesMapped;
    private final int baseColor;

    public TonalPalette(TonalSpec spec, int seedColor) {
        this.spec = spec;
        this.seedCam = Cam.fromInt(seedColor);
        this.allShades = spec.shades(seedCam);
        this.allShadesMapped = zipLists(TonalPalette.SHADE_KEYS, allShades);

        float h = (float)spec.getHue().get(seedCam);
        float c = (float)spec.getChroma().get(seedCam);
        this.baseColor = ColorUtils.CAMToColor(h, c, CamUtils.lstarFromInt(seedColor));
    }

    public int getAtTone(float tone) {
        return spec.getAtTone(seedCam, tone);
    }

    public int getS10() {
        return allShades.get(0);
    }

    public int getS50() {
        return allShades.get(1);
    }

    public int getS100() {
        return allShades.get(2);
    }

    public int getS200() {
        return allShades.get(3);
    }

    public int getS300() {
        return allShades.get(4);
    }

    public int getS400() {
        return allShades.get(5);
    }

    public int getS500() {
        return allShades.get(6);
    }

    public int getS600() {
        return allShades.get(7);
    }

    public int getS700() {
        return allShades.get(8);
    }

    public int getS800() {
        return allShades.get(9);
    }

    public int getS900() {
        return allShades.get(10);
    }

    public int getS1000() {
        return allShades.get(11);
    }

    private static Map<Integer, Integer> zipLists(List<Integer> keys, List<Integer> values) {
        Map<Integer, Integer> map = new HashMap<>();
        int size = Math.min(keys.size(), values.size());
        for (int i = 0; i < size; i++) {
            map.put(keys.get(i), values.get(i));
        }
        return map;
    }

    public static final List<Integer> SHADE_KEYS = new ArrayList<>(
            List.of(10, 50, 100, 200, 300, 400, 500, 600, 700, 800, 900, 1000)
    );
}
