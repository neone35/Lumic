package com.arturmaslov.lumic.utils

import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp

fun Int.modifyColor(factor: Float): Int {
    require(factor > 0f) { "Factor must be greater than 0" }

    val originalColor = Color(this)
    val targetColor = if (factor < 1f) {
        // Darken the color (lerp towards black)
        lerp(originalColor, Color.Black, 1 - factor)
    } else {
        // Brighten the color (lerp towards white)
        lerp(originalColor, Color.White, factor - 1)
    }
    return android.graphics.Color.argb(
        (targetColor.alpha * 255).toInt(),
        (targetColor.red * 255).toInt(),
        (targetColor.green * 255).toInt(),
        (targetColor.blue * 255).toInt()
    )
}

fun Context.getAppName(): String = applicationInfo.loadLabel(packageManager).toString()

fun Color.toHex(): String {
    val red = (red * 255).toInt()
    val green = (green * 255).toInt()
    val blue = (blue * 255).toInt()
//    val alpha = (alpha * 255).toInt()
    return String.format("#%02X%02X%02X", red, green, blue)
}