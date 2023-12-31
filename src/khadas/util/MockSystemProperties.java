package com.khadas.util;

public class MockSystemProperties implements ISystemProperties{
    @Override
    public int getInt(String key, int def) {
        return 0;
    }

    @Override
    public String get(String key) {
        return null;
    }

    @Override
    public void set(String key, String value) {

    }
}
