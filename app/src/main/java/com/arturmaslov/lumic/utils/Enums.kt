package com.arturmaslov.lumic.utils

enum class FlashMode(val string: String) {
    BOTH("both"),
    FLASH("flash"),
    SCREEN("screen"),
    NONE("none"),
    STROBE("strobe")
}

enum class ColorMode(val value: Float) {
    DARKER(0.6f),
    LIGHTER(1.6f)
}

enum class LoadStatus {
    LOADING,
    ERROR,
    DONE
}

enum class PermissionStatus(val bool: Boolean) {
    GRANTED (true),
    DENIED (false)
}