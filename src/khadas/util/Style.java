package com.khadas.util;

public enum Style {
    SPRITZ(
            new CoreSpec(
                    new TonalSpec(new HueSource(), new ChromaConstant(12.0)),
                    new TonalSpec(new HueSource(), new ChromaConstant(8.0)),
                    new TonalSpec(new HueSource(), new ChromaConstant(16.0)),
                    new TonalSpec(new HueSource(), new ChromaConstant(2.0)),
                    new TonalSpec(new HueSource(), new ChromaConstant(2.0))
            )
    ),
    TONAL_SPOT(
            new CoreSpec(
                    new TonalSpec(new HueSource(), new ChromaConstant(36.0)),
                    new TonalSpec(new HueSource(), new ChromaConstant(16.0)),
                    new TonalSpec(new HueAdd(60.0), new ChromaConstant(24.0)),
                    new TonalSpec(new HueSource(), new ChromaConstant(6.0)),
                    new TonalSpec(new HueSource(), new ChromaConstant(8.0))
            )
    ),
    VIBRANT(
            new CoreSpec(
                    new TonalSpec(new HueSource(), new ChromaMaxOut()),
                    new TonalSpec(new HueVibrantSecondary(), new ChromaConstant(24.0)),
                    new TonalSpec(new HueVibrantTertiary(), new ChromaConstant(32.0)),
                    new TonalSpec(new HueSource(), new ChromaConstant(10.0)),
                    new TonalSpec(new HueSource(), new ChromaConstant(12.0))
            )
    ),
    EXPRESSIVE(
            new CoreSpec(
                    new TonalSpec(new HueAdd(240.0), new ChromaConstant(40.0)),
                    new TonalSpec(new HueExpressiveSecondary(), new ChromaConstant(24.0)),
                    new TonalSpec(new HueExpressiveTertiary(), new ChromaConstant(32.0)),
                    new TonalSpec(new HueAdd(15.0), new ChromaConstant(8.0)),
                    new TonalSpec(new HueAdd(15.0), new ChromaConstant(12.0))
            )
    ),
    RAINBOW(
            new CoreSpec(
                    new TonalSpec(new HueSource(), new ChromaConstant(48.0)),
                    new TonalSpec(new HueSource(), new ChromaConstant(16.0)),
                    new TonalSpec(new HueAdd(60.0), new ChromaConstant(24.0)),
                    new TonalSpec(new HueSource(), new ChromaConstant(0.0)),
                    new TonalSpec(new HueSource(), new ChromaConstant(0.0))
            )
    ),
    FRUIT_SALAD(
            new CoreSpec(
                    new TonalSpec(new HueSubtract(50.0), new ChromaConstant(48.0)),
                    new TonalSpec(new HueSubtract(50.0), new ChromaConstant(36.0)),
                    new TonalSpec(new HueSource(), new ChromaConstant(36.0)),
                    new TonalSpec(new HueSource(), new ChromaConstant(10.0)),
                    new TonalSpec(new HueSource(), new ChromaConstant(16.0))
            )
    ),
    CONTENT(
            new CoreSpec(
                    new TonalSpec(new HueSource(), new ChromaSource()),
                    new TonalSpec(new HueSource(), new ChromaMultiple(0.33)),
                    new TonalSpec(new HueSource(), new ChromaMultiple(0.66)),
                    new TonalSpec(new HueSource(), new ChromaMultiple(0.0833)),
                    new TonalSpec(new HueSource(), new ChromaMultiple(0.1666))
            )
    ),
    MONOCHROMATIC(
            new CoreSpec(
                    new TonalSpec(new HueSource(), new ChromaConstant(.0)),
                    new TonalSpec(new HueSource(), new ChromaConstant(.0)),
                    new TonalSpec(new HueSource(), new ChromaConstant(.0)),
                    new TonalSpec(new HueSource(), new ChromaConstant(.0)),
                    new TonalSpec(new HueSource(), new ChromaConstant(.0))
            )
    ),
    CLOCK(
            new CoreSpec(
                    new TonalSpec(new HueSource(), new ChromaBound(new ChromaSource(), 20.0, Chroma.MAX_VALUE)),
                    new TonalSpec(new HueAdd(10.0), new ChromaBound(new ChromaMultiple(0.85), 17.0, 40.0)),
                    new TonalSpec(new HueAdd(20.0), new ChromaBound(new ChromaAdd(20.0), 50.0, Chroma.MAX_VALUE)),
                    new TonalSpec(new HueSource(), new ChromaConstant(0.0)),
                    new TonalSpec(new HueSource(), new ChromaConstant(0.0))
            )
    ),
    CLOCK_VIBRANT(
            new CoreSpec(
                    new TonalSpec(new HueSource(), new ChromaBound(new ChromaSource(), 70.0, Chroma.MAX_VALUE)),
                    new TonalSpec(new HueAdd(20.0), new ChromaBound(new ChromaSource(), 70.0, Chroma.MAX_VALUE)),
                    new TonalSpec(new HueAdd(60.0), new ChromaBound(new ChromaSource(), 70.0, Chroma.MAX_VALUE)),
                    new TonalSpec(new HueSource(), new ChromaConstant(0.0)),
                    new TonalSpec(new HueSource(), new ChromaConstant(0.0))
            )
    );

    final CoreSpec coreSpec;

    Style(CoreSpec coreSpec) {
        this.coreSpec = coreSpec;
    }
}
