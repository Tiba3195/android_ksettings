package com.khadas.ksettings;

public class GPIOPin {
    private int pinNumber;
    private Direction direction;
    private PinValue value;

    public GPIOPin(int pinNumber, Direction direction, PinValue value) {
        this.pinNumber = pinNumber;
        this.direction = direction;
        this.value = value;
    }

    public int getPinNumber() {
        return pinNumber;
    }

    public Direction getDirection() {
        return direction;
    }

    public PinValue getValue() {
        return value;
    }

    // Add setters if needed
}