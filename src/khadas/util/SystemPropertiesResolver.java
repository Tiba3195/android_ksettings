package com.khadas.util;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class SystemPropertiesResolver {
    private static ISystemProperties instance;

    public static ISystemProperties get() {
        if (instance == null) {
            try {
                // Try to use the actual SystemProperties class using reflection
                Class<?> clazz = Class.forName("android.os.SystemProperties");
                instance = (ISystemProperties) Proxy.newProxyInstance(
                        clazz.getClassLoader(),
                        new Class<?>[]{ISystemProperties.class},
                        new InvocationHandler() {
                            @Override
                            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                                Method realMethod = clazz.getMethod(method.getName(), method.getParameterTypes());
                                return realMethod.invoke(null, args);
                            }
                        });
            } catch (Exception e) {
                // Fallback to the mock implementation
                instance = new MockSystemProperties();
            }
        }
        return instance;
    }
}
