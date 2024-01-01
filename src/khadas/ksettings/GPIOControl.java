package com.khadas.ksettings;

public class GPIOControl {

    static {
        System.loadLibrary("gpiocontrol");  // Name of your .so library
    }

    public static native int exportPin(int pin);

    public static native int unexportPin(int pin);

    public static native int setPinDirection(int pin, int direction);  // Direction: 0 for IN, 1 for OUT

    public static native int setPinValue(int pin, int value);  // Value: 0 for LOW, 1 for HIGH

    public static native int getPinValue(int pin);
}