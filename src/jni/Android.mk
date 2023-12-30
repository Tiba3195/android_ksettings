LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
LOCAL_MODULE := libjni_ksettings

LOCAL_SRC_FILES := ksettings.cpp

LOCAL_C_INCLUDES := $(LOCAL_PATH)/includes \
	prebuilts/jdk/jdk8/linux-x86/include \
    prebuilts/jdk/jdk8/linux-x86/include/linux

LOCAL_SHARED_LIBRARIES := \
    libandroid \
    liblog \
    libdl \
    gpiocontrol \
	libnativehelper

LOCAL_CFLAGS += \
    -ffast-math \
    -O3 \
    -funroll-loops \
    -Wall \
    -Wextra \
    -Werror

LOCAL_CPPFLAGS += -std=c++11 # Adjust as needed for your C++ standard

LOCAL_LDLIBS := -llog -ldl

LOCAL_MODULE_PATH := $(TARGET_OUT_SHARED_LIBRARIES)

LOCAL_TARGET_ARCH_ABI := arm64-v8a

include $(BUILD_SHARED_LIBRARY)

