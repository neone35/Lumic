package com.arturmaslov.lumic.utils

object Constants {
    const val FLASH_ON_DURATION_INITIAL: Float = 200f;
    const val FLASH_ON_DURATION_STEPS: Int = 10;
    const val FLASH_ON_DURATION_MIN: Float = 50f;
    const val FLASH_ON_DURATION_MAX: Float = 500f;

    const val AMPLITUDE_RECORD_INTERVAL_MS: Long = 100; // how many times to check amplitude during recording
    const val AUDIO_RECORD_INTERVAL_MS: Long = 500;
    const val DELAY_FROM_FLASH_TO_STOP_MS: Long = 50;
    const val STROBE_ON_DURATION = 1000L

    const val SENSITIVITY_THRESHOLD_INITIAL: Float = 400f;
    const val SENSITIVITY_THRESHOLD_STEPS: Int = 30;
    const val SENSITIVITY_THRESHOLD_MIN: Float = 100f;
    const val SENSITIVITY_THRESHOLD_MAX: Float = 1000f;
    const val COLOR_INITIAL: Int = 0xFF444444.toInt() //Color.DarkGray.value
    const val FLASH_MODE_INITIAL: String = "both"

    const val EMPTY_STRING: String = "";
    const val PREFS_DEFAULT = "prefs_default"

}