package com.arturmaslov.lumic.utils

import android.content.Context
import android.content.Context.CAMERA_SERVICE
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import com.arturmaslov.lumic.cache.FlashSettingCache
import com.arturmaslov.lumic.cache.SensitivitySettingCache
import com.arturmaslov.lumic.ui.compose.main.FlashModeState
import com.arturmaslov.lumic.utils.Constants.FLASH_MODE_INITIAL
import com.arturmaslov.lumic.utils.Constants.SENSITIVITY_THRESHOLD_INITIAL
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CameraUtils(
    context: Context,
    private val sensitivitySettingsCache: SensitivitySettingCache,
    private val flashSettingsCache: FlashSettingCache
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
            val dataSnapshot = volumeData.toList() // Create a copy of the list to avoid concurrent issue

            val currentSensitivityThreshold = sensitivitySettingsCache.get() ?: SENSITIVITY_THRESHOLD_INITIAL
            val currentFlashMode = flashSettingsCache.get()

            val dataNotEmpty = dataSnapshot.isNotEmpty()
            val highAmplitude = dataSnapshot.any { it > currentSensitivityThreshold }
            val bothAndFlash = when (currentFlashMode) {
                FlashModeState.NONE -> false
                FlashModeState.SCREEN -> false
                FlashModeState.BOTH -> true
                FlashModeState.FLASH -> true
                else -> false
            }

            if (dataNotEmpty && highAmplitude) {
                if (bothAndFlash) cameraManager.setTorchMode(cameraId, true) // Turn on
                timesFlashed.value++
                if (bothAndFlash) delay(Constants.FLASH_ON_DURATION_MS)
                if (bothAndFlash) cameraManager.setTorchMode(cameraId, false) // Turn off
            }
        }
    }

    fun timesFlashed() = timesFlashed as StateFlow<Int>
    fun hasFlash() = hasFlash as StateFlow<Boolean>
}