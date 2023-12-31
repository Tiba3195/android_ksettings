package com.khadas.ksettings;

public enum Direction {
    IN,
    OUT;

    // If you need to convert the enum to a string or integer, you can add methods here
    public String toString() {
        return this.name(); // Returns "IN" or "OUT"
    }
}
