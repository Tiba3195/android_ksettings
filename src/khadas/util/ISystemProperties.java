package com.khadas.util;

public interface ISystemProperties {
    int getInt(String key, int def);
    String get(String key);
    void set(String key, String value);
}
