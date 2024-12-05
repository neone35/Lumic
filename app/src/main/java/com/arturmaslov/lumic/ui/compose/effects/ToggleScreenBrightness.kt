package com.arturmaslov.lumic.ui.compose.effects

import android.view.Window
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

@Composable
fun ToggleScreenBrightness(window: Window, isMaxBrightness: Boolean) {
    LaunchedEffect(isMaxBrightness) {
        window.attributes = window.attributes.apply {
            screenBrightness = if (isMaxBrightness) 1F else -1F // Max brightness or system default
        }
    }
}
