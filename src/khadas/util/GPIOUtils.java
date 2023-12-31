package com.khadas.util;

import com.khadas.ksettings.Direction;
import com.khadas.ksettings.GPIOPin;
import com.khadas.ksettings.PinValue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GPIOUtils {

    public static List<GPIOPin> getExportedGPIOPins() {
        List<GPIOPin> exportedPins = new ArrayList<>();
        File gpioDir = new File("/sys/class/gpio");

        if (gpioDir.exists() && gpioDir.isDirectory()) {
            File[] files = gpioDir.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        String fileName = file.getName();
                        if (fileName.startsWith("gpio")) {
                            try {
                                int pinNumber = Integer.parseInt(fileName.substring(4));

                                // Fetch the actual direction and value from the sysfs
                                File pinDir = new File(gpioDir, fileName);
                                File directionFile = new File(pinDir, "direction");
                                File valueFile = new File(pinDir, "value");

                                Direction direction = readDirection(directionFile);
                                PinValue value = readValue(valueFile);

                                exportedPins.add(new GPIOPin(pinNumber, direction, value));
                            } catch (NumberFormatException e) {
                                // Handle exception if the name isn't a valid integer
                            }
                        }
                    }
                }
            }
        }

        return exportedPins;
    }

    private static Direction readDirection(File file) {
        String directionStr = readFile(file);
        return "in".equalsIgnoreCase(directionStr) ? Direction.IN : Direction.OUT;
    }

    private static PinValue readValue(File file) {
        String valueStr = readFile(file);
        return "0".equals(valueStr) ? PinValue.LOW : PinValue.HIGH;
    }

    private static String readFile(File file) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            return br.readLine();
        } catch (IOException e) {
            return ""; // Or handle the exception as needed
        }
    }
}