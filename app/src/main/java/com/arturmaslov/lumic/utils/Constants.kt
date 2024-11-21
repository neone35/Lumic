package com.arturmaslov.lumic.utils

import androidx.compose.ui.graphics.Color

object Constants {
    const val FLASH_ON_DURATION_MS: Long = 200;
    const val AMPLITUDE_RECORD_INTERVAL_MS: Long = 100;
    const val AUDIO_RECORD_INTERVAL_MS: Long = 500;
    const val DELAY_FROM_FLASH_TO_STOP_MS: Long = 50;

    const val SENSITIVITY_THRESHOLD_INITIAL: Float = 400f;
    const val SENSITIVITY_THRESHOLD_STEPS: Int = 30;
    const val SENSITIVITY_THRESHOLD_MIN: Float = 100f;
    const val SENSITIVITY_THRESHOLD_MAX: Float = 1000f;
    val COLOR_INITIAL: Long = Color.DarkGray.value.toLong();

    const val EMPTY_STRING: String = "";
    const val PREFS_DEFAULT = "prefs_default"

}