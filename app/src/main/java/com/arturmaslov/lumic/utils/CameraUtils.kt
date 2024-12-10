package com.arturmaslov.lumic.utils

import android.content.Context
import android.content.Context.CAMERA_SERVICE
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import com.arturmaslov.lumic.cache.FlashDurationSettingCache
import com.arturmaslov.lumic.cache.FlashSettingCache
import com.arturmaslov.lumic.cache.SensitivitySettingCache
import com.arturmaslov.lumic.utils.Constants.FLASH_ON_DURATION_INITIAL
import com.arturmaslov.lumic.utils.Constants.SENSITIVITY_THRESHOLD_INITIAL
import com.arturmaslov.lumic.utils.Constants.STROBE_ON_DURATION
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CameraUtils(
    context: Context,
    private val sensitivitySettingsCache: SensitivitySettingCache,
    private val flashSettingsCache: FlashSettingCache,
    private val flashDurationSettingCache: FlashDurationSettingCache
) {
    private var cameraManager: CameraManager =
        context.getSystemService(CAMERA_SERVICE) as CameraManager
    private var cameraId: String = cameraManager.cameraIdList[0]

    private var timesFlashed = MutableStateFlow(0)
    private var hasFlash = MutableStateFlow(false)

    fun checkIfHasFlash() {
        hasFlash.value = cameraManager.getCameraCharacteristics(cameraId)
            .get(CameraCharacteristics.FLASH_INFO_AVAILABLE) == true
    }

    fun flashLight(volumeData: List<Int>) {
        CoroutineScope(Dispatchers.IO).launch {
            val dataSnapshot =
                volumeData.toList() // Create a copy of the list to avoid concurrent issue

            val currentSensitivityThreshold =
                sensitivitySettingsCache.get() ?: SENSITIVITY_THRESHOLD_INITIAL
            val currentFlashMode = flashSettingsCache.get()
            val currentFlashDuration = flashDurationSettingCache.get() ?: FLASH_ON_DURATION_INITIAL

            val dataNotEmpty = dataSnapshot.isNotEmpty()
            val highAmplitude = dataSnapshot.any { it > currentSensitivityThreshold }
            val hasFlash = hasFlash.value
            val bothOrFlashMode = when (currentFlashMode) {
                FlashMode.BOTH  -> true && hasFlash
                FlashMode.FLASH -> true && hasFlash
                else -> false
            }
            val strobeFlashMode = currentFlashMode == FlashMode.STROBE && hasFlash

            if (strobeFlashMode) {
                cameraManager.setTorchMode(cameraId, true) // Turn on
                delay(STROBE_ON_DURATION)
                cameraManager.setTorchMode(cameraId, false) // Turn off
            } else if (dataNotEmpty && highAmplitude) {
                if (bothOrFlashMode) cameraManager.setTorchMode(cameraId, true) // Turn on
                timesFlashed.value++ // needed for screen flash also
                if (bothOrFlashMode) delay(currentFlashDuration.toLong())
                if (bothOrFlashMode) cameraManager.setTorchMode(cameraId, false) // Turn off
            }

        }
    }

    fun timesFlashed() = timesFlashed as StateFlow<Int>
    fun hasFlash() = hasFlash as StateFlow<Boolean>
}