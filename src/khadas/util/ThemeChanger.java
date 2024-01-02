package com.khadas.util;

import android.graphics.Color;
import java.io.OutputStream;
import java.io.IOException;

public class ThemeChanger {
    public enum ThemeStyle {
        TONAL_SPOT, // Default Material You theme since Android S.
        VIBRANT,    // Theme where accent 2 and 3 are analogous to accent 1.
        EXPRESSIVE, // Highly chromatic theme.
        SPRITZ,     // Desaturated theme, almost grayscale.
        RAINBOW,    // Additional custom theme style.
        FRUIT_SALAD, // Additional custom theme style.
        MONOCHROMATIC; // Additional custom theme style.,

        // Convert enum to String
        public String toString() {
            return this.name();
        }

        // Convert String to enum
        public static ThemeStyle fromString(String text) {
            for (ThemeStyle style : ThemeStyle.values()) {
                if (style.name().equalsIgnoreCase(text)) {
                    return style;
                }
            }
            throw new IllegalArgumentException("No constant with text " + text + " found");
        }
    }



    public static void setThemeColor(int seed, ThemeStyle themeStyle) {
        String palette = convertColorToHex(seed);
        String command = buildCommand(palette, themeStyle);
        executeShellCommand(command);
    }

    public static void setTheme(Color color, ThemeStyle style) {
        String palette = convertColorToHex(color.toArgb());
        String command = buildCommand(palette, style);
        executeShellCommand(command);
    }

    public static void setTheme(int color, ThemeStyle style) {
        String palette = convertColorToHex(color);
        String command = buildCommand(palette, style);
        executeShellCommand(command);
    }

    private static String convertColorToHex(int color) {
        return String.format("#%06X", (0xFFFFFF & color));
    }

    private static String buildCommand(String palette, ThemeStyle style) {
        return "settings put secure theme_customization_overlay_packages '{\"android.theme.customization.system_palette\":\"" + palette + "\", \"android.theme.customization.theme_style\":\"" + style.name() + "\"}'";
    }

    private static void executeShellCommand(String command) {
        Process process = null;
        OutputStream os = null;
        try {
            process = Runtime.getRuntime().exec("su");
            os = process.getOutputStream();
            os.write((command + "\n").getBytes());
            os.write("exit\n".getBytes());
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                process.destroy();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
