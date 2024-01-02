package com.khadas.util


import android.annotation.SuppressLint

import android.app.WallpaperColors
import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi

import kotlin.math.absoluteValue
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

const val TAG = "ColorScheme"

const val ACCENT1_CHROMA = 48.0f
const val GOOGLE_BLUE = 0xFF1b6ef3.toInt()
const val MIN_CHROMA = 5

public interface Hue {
    fun get(sourceColor: Cam): Double

    /**
     * Given a hue, and a mapping of hues to hue rotations, find which hues in the mapping the hue
     * fall betweens, and use the hue rotation of the lower hue.
     *
     * @param sourceHue hue of source color
     * @param hueAndRotations list of pairs, where the first item in a pair is a hue, and the second
     *   item in the pair is a hue rotation that should be applied
     */
    fun getHueRotation(sourceHue: Float, hueAndRotations: List<Pair<Int, Int>>): Double {
        val sanitizedSourceHue = (if (sourceHue < 0 || sourceHue >= 360) 0 else sourceHue).toFloat()
        for (i in 0..hueAndRotations.size - 2) {
            val thisHue = hueAndRotations[i].first.toFloat()
            val nextHue = hueAndRotations[i + 1].first.toFloat()
            if (thisHue <= sanitizedSourceHue && sanitizedSourceHue < nextHue) {
                return ColorScheme.wrapDegreesDouble(
                    sanitizedSourceHue.toDouble() + hueAndRotations[i].second
                )
            }
        }

        // If this statement executes, something is wrong, there should have been a rotation
        // found using the arrays.
        return sourceHue.toDouble()
    }
}

public class HueSource : Hue {
    override fun get(sourceColor: Cam): Double {
        return sourceColor.hue.toDouble()
    }
}

public class HueAdd(val amountDegrees: Double) : Hue {
    override fun get(sourceColor: Cam): Double {
        return ColorScheme.wrapDegreesDouble(sourceColor.hue.toDouble() + amountDegrees)
    }
}

public class HueSubtract(val amountDegrees: Double) : Hue {
    override fun get(sourceColor: Cam): Double {
        return ColorScheme.wrapDegreesDouble(sourceColor.hue.toDouble() - amountDegrees)
    }
}

public class HueVibrantSecondary() : Hue {
    val hueToRotations =
        listOf(
            Pair(0, 18),
            Pair(41, 15),
            Pair(61, 10),
            Pair(101, 12),
            Pair(131, 15),
            Pair(181, 18),
            Pair(251, 15),
            Pair(301, 12),
            Pair(360, 12)
        )

    override fun get(sourceColor: Cam): Double {
        return getHueRotation(sourceColor.hue, hueToRotations)
    }
}

public class HueVibrantTertiary() : Hue {
    val hueToRotations =
        listOf(
            Pair(0, 35),
            Pair(41, 30),
            Pair(61, 20),
            Pair(101, 25),
            Pair(131, 30),
            Pair(181, 35),
            Pair(251, 30),
            Pair(301, 25),
            Pair(360, 25)
        )

    override fun get(sourceColor: Cam): Double {
        return getHueRotation(sourceColor.hue, hueToRotations)
    }
}

public class HueExpressiveSecondary() : Hue {
    val hueToRotations =
        listOf(
            Pair(0, 45),
            Pair(21, 95),
            Pair(51, 45),
            Pair(121, 20),
            Pair(151, 45),
            Pair(191, 90),
            Pair(271, 45),
            Pair(321, 45),
            Pair(360, 45)
        )

    override fun get(sourceColor: Cam): Double {
        return getHueRotation(sourceColor.hue, hueToRotations)
    }
}

public class HueExpressiveTertiary() : Hue {
    val hueToRotations =
        listOf(
            Pair(0, 120),
            Pair(21, 120),
            Pair(51, 20),
            Pair(121, 45),
            Pair(151, 20),
            Pair(191, 15),
            Pair(271, 20),
            Pair(321, 120),
            Pair(360, 120)
        )

    override fun get(sourceColor: Cam): Double {
        return getHueRotation(sourceColor.hue, hueToRotations)
    }
}

public interface Chroma {
    fun get(sourceColor: Cam): Double

    companion object {
        val MAX_VALUE = 120.0
        val MIN_VALUE = 0.0
    }
}

public class ChromaMaxOut : Chroma {
    override fun get(sourceColor: Cam): Double {
        // Intentionally high. Gamut mapping from impossible HCT to sRGB will ensure that
        // the maximum chroma is reached, even if lower than this constant.
        return Chroma.MAX_VALUE + 10.0
    }
}

public class ChromaMultiple(val multiple: Double) : Chroma {
    override fun get(sourceColor: Cam): Double {
        return sourceColor.chroma * multiple
    }
}

public class ChromaAdd(val amount: Double) : Chroma {
    override fun get(sourceColor: Cam): Double {
        return sourceColor.chroma + amount
    }
}

public class ChromaBound(
    val baseChroma: Chroma,
    val minVal: Double,
    val maxVal: Double,
) : Chroma {
    override fun get(sourceColor: Cam): Double {
        val result = baseChroma.get(sourceColor)
        return min(max(result, minVal), maxVal)
    }
}

public class ChromaConstant(val chroma: Double) : Chroma {
    override fun get(sourceColor: Cam): Double {
        return chroma
    }
}

public class ChromaSource : Chroma {
    override fun get(sourceColor: Cam): Double {
        return sourceColor.chroma.toDouble()
    }
}

public class TonalSpec(val hue: Hue = HueSource(), val chroma: Chroma) {
    fun shades(sourceColor: Cam): List<Int> {
        val hue = hue.get(sourceColor)
        val chroma = chroma.get(sourceColor)
        return Shades.of(hue.toFloat(), chroma.toFloat()).toList()
    }

    fun getAtTone(sourceColor: Cam, tone: Float): Int {
        val hue = hue.get(sourceColor)
        val chroma = chroma.get(sourceColor)
        return ColorUtils.CAMToColor(hue.toFloat(), chroma.toFloat(), (1000f - tone) / 10f)
    }
}

public class CoreSpec(
    val a1: TonalSpec,
    val a2: TonalSpec,
    val a3: TonalSpec,
    val n1: TonalSpec,
    val n2: TonalSpec
)

enum class Style(internal val coreSpec: CoreSpec) {
    SPRITZ(
        CoreSpec(
            a1 = TonalSpec(HueSource(), ChromaConstant(12.0)),
            a2 = TonalSpec(HueSource(), ChromaConstant(8.0)),
            a3 = TonalSpec(HueSource(), ChromaConstant(16.0)),
            n1 = TonalSpec(HueSource(), ChromaConstant(2.0)),
            n2 = TonalSpec(HueSource(), ChromaConstant(2.0))
        )
    ),
    TONAL_SPOT(
        CoreSpec(
            a1 = TonalSpec(HueSource(), ChromaConstant(36.0)),
            a2 = TonalSpec(HueSource(), ChromaConstant(16.0)),
            a3 = TonalSpec(HueAdd(60.0), ChromaConstant(24.0)),
            n1 = TonalSpec(HueSource(), ChromaConstant(6.0)),
            n2 = TonalSpec(HueSource(), ChromaConstant(8.0))
        )
    ),
    VIBRANT(
        CoreSpec(
            a1 = TonalSpec(HueSource(), ChromaMaxOut()),
            a2 = TonalSpec(HueVibrantSecondary(), ChromaConstant(24.0)),
            a3 = TonalSpec(HueVibrantTertiary(), ChromaConstant(32.0)),
            n1 = TonalSpec(HueSource(), ChromaConstant(10.0)),
            n2 = TonalSpec(HueSource(), ChromaConstant(12.0))
        )
    ),
    EXPRESSIVE(
        CoreSpec(
            a1 = TonalSpec(HueAdd(240.0), ChromaConstant(40.0)),
            a2 = TonalSpec(HueExpressiveSecondary(), ChromaConstant(24.0)),
            a3 = TonalSpec(HueExpressiveTertiary(), ChromaConstant(32.0)),
            n1 = TonalSpec(HueAdd(15.0), ChromaConstant(8.0)),
            n2 = TonalSpec(HueAdd(15.0), ChromaConstant(12.0))
        )
    ),
    RAINBOW(
        CoreSpec(
            a1 = TonalSpec(HueSource(), ChromaConstant(48.0)),
            a2 = TonalSpec(HueSource(), ChromaConstant(16.0)),
            a3 = TonalSpec(HueAdd(60.0), ChromaConstant(24.0)),
            n1 = TonalSpec(HueSource(), ChromaConstant(0.0)),
            n2 = TonalSpec(HueSource(), ChromaConstant(0.0))
        )
    ),
    FRUIT_SALAD(
        CoreSpec(
            a1 = TonalSpec(HueSubtract(50.0), ChromaConstant(48.0)),
            a2 = TonalSpec(HueSubtract(50.0), ChromaConstant(36.0)),
            a3 = TonalSpec(HueSource(), ChromaConstant(36.0)),
            n1 = TonalSpec(HueSource(), ChromaConstant(10.0)),
            n2 = TonalSpec(HueSource(), ChromaConstant(16.0))
        )
    ),
    CONTENT(
        CoreSpec(
            a1 = TonalSpec(HueSource(), ChromaSource()),
            a2 = TonalSpec(HueSource(), ChromaMultiple(0.33)),
            a3 = TonalSpec(HueSource(), ChromaMultiple(0.66)),
            n1 = TonalSpec(HueSource(), ChromaMultiple(0.0833)),
            n2 = TonalSpec(HueSource(), ChromaMultiple(0.1666))
        )
    ),
    MONOCHROMATIC(
        CoreSpec(
            a1 = TonalSpec(HueSource(), ChromaConstant(.0)),
            a2 = TonalSpec(HueSource(), ChromaConstant(.0)),
            a3 = TonalSpec(HueSource(), ChromaConstant(.0)),
            n1 = TonalSpec(HueSource(), ChromaConstant(.0)),
            n2 = TonalSpec(HueSource(), ChromaConstant(.0))
        )
    ),
    CLOCK(
        CoreSpec(
            a1 = TonalSpec(HueSource(), ChromaBound(ChromaSource(), 20.0, Chroma.MAX_VALUE)),
            a2 = TonalSpec(HueAdd(10.0), ChromaBound(ChromaMultiple(0.85), 17.0, 40.0)),
            a3 = TonalSpec(HueAdd(20.0), ChromaBound(ChromaAdd(20.0), 50.0, Chroma.MAX_VALUE)),

            // Not Used
            n1 = TonalSpec(HueSource(), ChromaConstant(0.0)),
            n2 = TonalSpec(HueSource(), ChromaConstant(0.0))
        )
    ),
    CLOCK_VIBRANT(
        CoreSpec(
            a1 = TonalSpec(HueSource(), ChromaBound(ChromaSource(), 70.0, Chroma.MAX_VALUE)),
            a2 = TonalSpec(HueAdd(20.0), ChromaBound(ChromaSource(), 70.0, Chroma.MAX_VALUE)),
            a3 = TonalSpec(HueAdd(60.0), ChromaBound(ChromaSource(), 70.0, Chroma.MAX_VALUE)),

            // Not Used
            n1 = TonalSpec(HueSource(), ChromaConstant(0.0)),
            n2 = TonalSpec(HueSource(), ChromaConstant(0.0))
        )
    )
}

class TonalPalette
public constructor(
    private val spec: TonalSpec,
    seedColor: Int,
) {
    val seedCam: Cam = Cam.fromInt(seedColor)
    val allShades: List<Int> = spec.shades(seedCam)
    val allShadesMapped: Map<Int, Int> = SHADE_KEYS.zip(allShades).toMap()
    val baseColor: Int

    init {
        val h = spec.hue.get(seedCam).toFloat()
        val c = spec.chroma.get(seedCam).toFloat()
        baseColor = ColorUtils.CAMToColor(h, c, CamUtils.lstarFromInt(seedColor))
    }

    // Dynamically computed tones across the full range from 0 to 1000
    fun getAtTone(tone: Float) = spec.getAtTone(seedCam, tone)

    // Predefined & precomputed tones
    val s10: Int
        get() = this.allShades[0]
    val s50: Int
        get() = this.allShades[1]
    val s100: Int
        get() = this.allShades[2]
    val s200: Int
        get() = this.allShades[3]
    val s300: Int
        get() = this.allShades[4]
    val s400: Int
        get() = this.allShades[5]
    val s500: Int
        get() = this.allShades[6]
    val s600: Int
        get() = this.allShades[7]
    val s700: Int
        get() = this.allShades[8]
    val s800: Int
        get() = this.allShades[9]
    val s900: Int
        get() = this.allShades[10]
    val s1000: Int
        get() = this.allShades[11]

    companion object {
        val SHADE_KEYS = listOf(10, 50, 100, 200, 300, 400, 500, 600, 700, 800, 900, 1000)
    }
}

class ColorScheme(
    @ColorInt val seed: Int,
    val darkTheme: Boolean,
    val style: Style = Style.TONAL_SPOT
) {

    val accent1: TonalPalette
    val accent2: TonalPalette
    val accent3: TonalPalette
    val neutral1: TonalPalette
    val neutral2: TonalPalette

    constructor(@ColorInt seed: Int, darkTheme: Boolean) : this(seed, darkTheme, Style.TONAL_SPOT)

    @RequiresApi(Build.VERSION_CODES.O_MR1)
    @JvmOverloads
    constructor(
        allColors: Map<Int, Int>, mainColors: List<Color>,
        darkTheme: Boolean,
        style: Style = Style.TONAL_SPOT
    ) : this(getSeedColor(allColors,mainColors, style != Style.CONTENT), darkTheme, style)

    @RequiresApi(Build.VERSION_CODES.O_MR1)
    @JvmOverloads
    constructor(
        wallpaperColors: WallpaperColors,
        darkTheme: Boolean,
        style: Style = Style.TONAL_SPOT
    ) : this(getSeedColorFromWallPaper(wallpaperColors, style != Style.CONTENT), darkTheme, style)

    val allHues: List<TonalPalette>
        get() {
            return listOf(accent1, accent2, accent3, neutral1, neutral2)
        }

    val allAccentColors: List<Int>
        get() {
            val allColors = mutableListOf<Int>()
            allColors.addAll(accent1.allShades)
            allColors.addAll(accent2.allShades)
            allColors.addAll(accent3.allShades)
            return allColors
        }

    val allNeutralColors: List<Int>
        get() {
            val allColors = mutableListOf<Int>()
            allColors.addAll(neutral1.allShades)
            allColors.addAll(neutral2.allShades)
            return allColors
        }

    val backgroundColor
        get() = ColorUtils.setAlphaComponent(if (darkTheme) neutral1.s700 else neutral1.s10, 0xFF)

    val accentColor
        get() = ColorUtils.setAlphaComponent(if (darkTheme) accent1.s100 else accent1.s500, 0xFF)

    init {
        val proposedSeedCam = Cam.fromInt(seed)
        val seedArgb =
            if (seed == Color.TRANSPARENT) {
                GOOGLE_BLUE
            } else if (style != Style.CONTENT && proposedSeedCam.chroma < 5) {
                GOOGLE_BLUE
            } else {
                seed
            }

        accent1 = TonalPalette(style.coreSpec.a1, seedArgb)
        accent2 = TonalPalette(style.coreSpec.a2, seedArgb)
        accent3 = TonalPalette(style.coreSpec.a3, seedArgb)
        neutral1 = TonalPalette(style.coreSpec.n1, seedArgb)
        neutral2 = TonalPalette(style.coreSpec.n2, seedArgb)
    }

    val shadeCount
        get() = this.accent1.allShades.size

    val seedTone: Float
        get() = 1000f - CamUtils.lstarFromInt(seed) * 10f

    override fun toString(): String {
        return "ColorScheme {\n" +
                "  seed color: ${stringForColor(seed)}\n" +
                "  style: $style\n" +
                "  palettes: \n" +
                "  ${humanReadable("PRIMARY", accent1.allShades)}\n" +
                "  ${humanReadable("SECONDARY", accent2.allShades)}\n" +
                "  ${humanReadable("TERTIARY", accent3.allShades)}\n" +
                "  ${humanReadable("NEUTRAL", neutral1.allShades)}\n" +
                "  ${humanReadable("NEUTRAL VARIANT", neutral2.allShades)}\n" +
                "}"
    }

    companion object {
        /**
         * Identifies a color to create a color scheme from.
         *
         * @param wallpaperColors Colors extracted from an image via quantization.
         * @param filter If false, allow colors that have low chroma, creating grayscale themes.
         * @return ARGB int representing the color
         */
        @RequiresApi(Build.VERSION_CODES.O_MR1)
        @JvmStatic
        @JvmOverloads
        @ColorInt
        fun getSeedColor( allColors: Map<Int, Int>, mainColors: List<Color>, filter: Boolean = true): Int {
            return  getSeedColors(allColors, mainColors, filter).first()
        }

        /**
         * Identifies a color to create a color scheme from.
         *
         * @param wallpaperColors Colors extracted from an image via quantization.
         * @param filter If false, allow colors that have low chroma, creating grayscale themes.
         * @return ARGB int representing the color
         */
        @RequiresApi(Build.VERSION_CODES.O_MR1)
        @JvmStatic
        @JvmOverloads
        @ColorInt
        fun getSeedColorFromWallPaper(wallpaperColors: WallpaperColors, filter: Boolean = true): Int {
            return getSeedColorsFromWallPaper(wallpaperColors, filter).first()
        }

        @RequiresApi(Build.VERSION_CODES.O_MR1)
        @SuppressLint("BlockedPrivateApi", "SoonBlockedPrivateApi")
        @JvmStatic
        @JvmOverloads
        fun getSeedColorsFromWallPaper(wallpaperColors: WallpaperColors, filter: Boolean = true): List<Int> {
            val getAllColorsMethod = WallpaperColors::class.java.getDeclaredMethod("getAllColors")
            getAllColorsMethod.isAccessible = true
            val allColors = getAllColorsMethod.invoke(wallpaperColors) as Map<Int, Int>

            val getMainColorsMethod = WallpaperColors::class.java.getDeclaredMethod("getMainColors")
            getMainColorsMethod.isAccessible = true
            val mainColors = getMainColorsMethod.invoke(wallpaperColors) as List<Color>

          return  getSeedColors(allColors, mainColors, filter)
        }
        /**
         * Filters and ranks colors from WallpaperColors.
         *
         * @param wallpaperColors Colors extracted from an image via quantization.
         * @param filter If false, allow colors that have low chroma, creating grayscale themes.
         * @return List of ARGB ints, ordered from highest scoring to lowest.
         */
        @RequiresApi(Build.VERSION_CODES.O_MR1)
        @SuppressLint("BlockedPrivateApi", "SoonBlockedPrivateApi")
        @JvmStatic
        @JvmOverloads
        fun getSeedColors( allColors: Map<Int, Int>, mainColors: List<Color>, filter: Boolean = true): List<Int> {

            val totalPopulation = allColors.values.sum().toDouble()
            val totalPopulationMeaningless = (totalPopulation == 0.0)

            if (totalPopulationMeaningless) {

                val distinctColors = mainColors
                    .map { it.toArgb() }
                    .distinct()
                    .filter {
                        if (!filter) true else Cam.fromInt(it).chroma >= MIN_CHROMA
                    }
                    .toList()

                if (distinctColors.isEmpty()) return listOf(GOOGLE_BLUE)
                return distinctColors
            }

            val intToProportion = allColors.mapValues { it.value.toDouble() / totalPopulation }
            val intToCam = allColors.mapValues { Cam.fromInt(it.key) }

            // Get an array with 360 slots. A slot contains the percentage of colors with that hue.
            val hueProportions = huePopulations(intToCam, intToProportion, filter)
            // Map each color to the percentage of the image with its hue.
            val intToHueProportion =
                allColors.mapValues {
                    val cam = intToCam[it.key]!!
                    val hue = cam.hue.roundToInt()
                    var proportion = 0.0
                    for (i in hue - 15..hue + 15) {
                        proportion += hueProportions[wrapDegrees(i)]
                    }
                    proportion
                }
            // Remove any inappropriate seed colors. For example, low chroma colors look grayscale
            // raising their chroma will turn them to a much louder color that may not have been
            // in the image.
            val filteredIntToCam =
                if (!filter) intToCam
                else
                    (intToCam.filter {
                        val cam = it.value
                        val proportion = intToHueProportion[it.key]!!
                        cam.chroma >= MIN_CHROMA &&
                                (totalPopulationMeaningless || proportion > 0.01)
                    })
            // Sort the colors by score, from high to low.
            val intToScoreIntermediate =
                filteredIntToCam.mapValues { score(it.value, intToHueProportion[it.key]!!) }
            val intToScore = intToScoreIntermediate.entries.toMutableList()
            intToScore.sortByDescending { it.value }

            // Go through the colors, from high score to low score.
            // If the color is distinct in hue from colors picked so far, pick the color.
            // Iteratively decrease the amount of hue distinctness required, thus ensuring we
            // maximize difference between colors.
            val minimumHueDistance = 15
            val seeds = mutableListOf<Int>()
            maximizeHueDistance@ for (i in 90 downTo minimumHueDistance step 1) {
                seeds.clear()
                for (entry in intToScore) {
                    val int = entry.key
                    val existingSeedNearby =
                        seeds.find {
                            val hueA = intToCam[int]!!.hue
                            val hueB = intToCam[it]!!.hue
                            hueDiff(hueA, hueB) < i
                        } != null
                    if (existingSeedNearby) {
                        continue
                    }
                    seeds.add(int)
                    if (seeds.size >= 4) {
                        break@maximizeHueDistance
                    }
                }
            }

            if (seeds.isEmpty()) {
                // Use gBlue 500 if there are 0 colors
                seeds.add(GOOGLE_BLUE)
            }

            return seeds
        }

        private fun wrapDegrees(degrees: Int): Int {
            return when {
                degrees < 0 -> {
                    (degrees % 360) + 360
                }
                degrees >= 360 -> {
                    degrees % 360
                }
                else -> {
                    degrees
                }
            }
        }

        public fun wrapDegreesDouble(degrees: Double): Double {
            return when {
                degrees < 0 -> {
                    (degrees % 360) + 360
                }
                degrees >= 360 -> {
                    degrees % 360
                }
                else -> {
                    degrees
                }
            }
        }

        private fun hueDiff(a: Float, b: Float): Float {
            return 180f - ((a - b).absoluteValue - 180f).absoluteValue
        }

        private fun stringForColor(color: Int): String {
            val width = 4
            val hct = Cam.fromInt(color)
            val h = "H${hct.hue.roundToInt().toString().padEnd(width)}"
            val c = "C${hct.chroma.roundToInt().toString().padEnd(width)}"
            val t = "T${CamUtils.lstarFromInt(color).roundToInt().toString().padEnd(width)}"
            val hex = Integer.toHexString(color and 0xffffff).padStart(6, '0').uppercase()
            return "$h$c$t = #$hex"
        }

        private fun humanReadable(paletteName: String, colors: List<Int>): String {
            return "$paletteName\n" +
                    colors.map { stringForColor(it) }.joinToString(separator = "\n") { it }
        }

        private fun score(cam: Cam, proportion: Double): Double {
            val proportionScore = 0.7 * 100.0 * proportion
            val chromaScore =
                if (cam.chroma < ACCENT1_CHROMA) 0.1 * (cam.chroma - ACCENT1_CHROMA)
                else 0.3 * (cam.chroma - ACCENT1_CHROMA)
            return chromaScore + proportionScore
        }

        private fun huePopulations(
            camByColor: Map<Int, Cam>,
            populationByColor: Map<Int, Double>,
            filter: Boolean = true
        ): List<Double> {
            val huePopulation = List(size = 360, init = { 0.0 }).toMutableList()

            for (entry in populationByColor.entries) {
                val population = populationByColor[entry.key]!!
                val cam = camByColor[entry.key]!!
                val hue = cam.hue.roundToInt() % 360
                if (filter && cam.chroma <= MIN_CHROMA) {
                    continue
                }
                huePopulation[hue] = huePopulation[hue] + population
            }

            return huePopulation
        }
    }
}