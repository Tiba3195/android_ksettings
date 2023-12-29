// Write C++ code here.
//
// Do not forget to dynamically load the C++ library into your application.
//
// For instance,
//
// In MainActivity.java:
//    static {
//       System.loadLibrary("ksettings");
//    }
//
// Or, in MainActivity.kt:
//    companion object {
//      init {
//         System.loadLibrary("ksettings")
//      }
//    }


#include <jni.h>
#include <gpiocontrol.h>  // Include your GPIOControl header

extern "C" {

JNIEXPORT jint JNICALL
Java_com_khadas_ksettings_GPIOControl_exportPin(JNIEnv *, jclass, jint pin) {
    return GPIOControl::exportPin(static_cast<int>(pin));
}

JNIEXPORT jint JNICALL
Java_com_khadas_ksettings_GPIOControl_unexportPin(JNIEnv *, jclass, jint pin) {
    return GPIOControl::unexportPin(static_cast<int>(pin));
}

JNIEXPORT jint JNICALL
Java_com_khadas_ksettings_GPIOControl_setPinDirection(JNIEnv *, jclass, jint pin, jint direction) {
    return GPIOControl::setPinDirection(static_cast<int>(pin), static_cast<GPIOControl::Direction>(direction));
}

JNIEXPORT jint JNICALL
Java_com_khadas_ksettings_GPIOControl_setPinValue(JNIEnv *, jclass, jint pin, jint value) {
    return GPIOControl::setPinValue(static_cast<int>(pin), static_cast<GPIOControl::PinValue>(value));
}

JNIEXPORT jint JNICALL
Java_com_khadas_ksettings_GPIOControl_getPinValue(JNIEnv *, jclass, jint pin) {
    return static_cast<jint>(GPIOControl::getPinValue(static_cast<int>(pin)));
}

}