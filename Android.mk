LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)


LOCAL_PACKAGE_NAME := KSettings

# Source files
LOCAL_SRC_FILES := $(call all-java-files-under, src) \
                   $(call all-kotlin-files-under, src)

# Static libraries
LOCAL_STATIC_JAVA_LIBRARIES := \
    androidx-constraintlayout_constraintlayout \
    androidx.appcompat_appcompat \
    androidx.recyclerview_recyclerview \
    androidx.cardview_cardview

LOCAL_SHARED_LIBRARIES := libgpiocontrol

# JNI libraries
LOCAL_JNI_SHARED_LIBRARIES := libjni_ksettings

# Certificate
LOCAL_CERTIFICATE := platform

# Access to private platform APIs
LOCAL_PRIVATE_PLATFORM_APIS := true

# Additional flags
LOCAL_PRIVILEGED_MODULE := true
LOCAL_SYSTEM_EXT_SPECIFIC := true
LOCAL_PLATFORM_APIS := true

APP_ABI := arm64-v8a

include $(BUILD_PACKAGE)
