package com.arturmaslov.lumic.utils

enum class FlashMode(val string: String) {
    BOTH("both"), // flash and screen
    FLASH("flash"),
    SCREEN("screen"),
    NONE("none"), // nothing at all
    STROBE("strobe") // only flash as fast as possible
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