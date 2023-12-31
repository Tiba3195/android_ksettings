package com.khadas.ksettings;

public enum PinValue {
    LOW,
    HIGH;

    // Similarly, add methods if needed for conversion or display
    public String toString() {
        return this.name(); // Returns "LOW" or "HIGH"
    }
}