package com.khadas.util;

import android.graphics.Color;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
        BufferedReader reader = null;
        BufferedReader errorReader = null;
        Log.d("ThemeChanger", "executeShellCommand: " + command);

        try {
            process = Runtime.getRuntime().exec("su"); // Execute 'su' to gain root access
            os = process.getOutputStream();
            os.write((command + "\n").getBytes()); // Send your command
            os.write("exit\n".getBytes()); // Exit from 'su' shell
            os.flush();

            reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                Log.d("ThemeChanger", "Shell Output: " + line);
            }
            while ((line = errorReader.readLine()) != null) {
                Log.d("ThemeChanger", "Shell Error: " + line);
            }

        } catch (IOException e) {
            e.printStackTrace();
            Log.d("ThemeChanger", "IOException", e);
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                if (reader != null) {
                    reader.close();
                }
                if (errorReader != null) {
                    errorReader.close();
                }
                if (process != null) {
                    process.destroy();
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("ThemeChanger", "finally IOException", e);
            }
        }
    }

    static public Map<String, String> getThemeCustomizationSettings() {
        Log.d("ThemeSettings", "<=======================================>");
        Log.d("ThemeSettings", "Fetching theme customization settings");
        Process process = null;
        BufferedReader reader = null;
        Map<String, String> settings = new HashMap<>();

        try {
            process = Runtime.getRuntime().exec("settings get secure theme_customization_overlay_packages");
            reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                Log.d("ThemeSettings", "Read line: " + line);
                parseSettingsLine(line, settings);
            }

        } catch (IOException e) {
            Log.e("ThemeSettings", "IOException occurred", e);
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
                if (process != null) {
                    process.destroy();
                }
            } catch (IOException e) {
                Log.e("ThemeSettings", "IOException occurred in finally block", e);
                e.printStackTrace();
            }
        }

        Log.d("ThemeSettings", "Settings fetched: " + settings);
        Log.d("ThemeSettings", "<=======================================>");
        return settings;
    }

    static private void parseSettingsLine(String line, Map<String, String> settings) {
        Log.d("ThemeSettings", "<=======================================>");
        Log.d("SettingsParser", "Parsing line: " + line);
        line = line.trim();
        if (line.startsWith("{") && line.endsWith("}")) {
            Log.d("SettingsParser", "Line starts and ends with curly braces");
            line = line.substring(1, line.length() - 1); // Remove curly braces
            Log.d("SettingsParser", "Line after removing curly braces: " + line);

            String[] pairs = line.split(",\\s*");
            for (String pair : pairs) {
                Log.d("SettingsParser", "Processing pair: " + pair);
                String[] keyValue = pair.split(":");
                if (keyValue.length == 2) {
                    String key = keyValue[0].trim().replace("\"", ""); // Remove all double quotes from key
                    String value = keyValue[1].trim().replace("\"", ""); // Remove all double quotes from value

                    Log.d("SettingsParser", "Parsed key: " + key + ", value: " + value);
                    settings.put(key, value);
                } else {
                    Log.d("SettingsParser", "Invalid key-value pair: " + pair);
                }
            }
        } else {
            Log.d("SettingsParser", "Line does not start and end with curly braces");
        }

        Log.d("ThemeSettings", "<=======================================>");
    }
}
