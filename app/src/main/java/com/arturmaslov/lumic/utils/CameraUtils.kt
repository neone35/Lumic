package com.arturmaslov.lumic.utils

import android.content.Context
import android.content.Context.CAMERA_SERVICE
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import com.arturmaslov.lumic.cache.SensitivitySettingCache
import com.arturmaslov.lumic.cache.SensitivitySettingCacheImpl
import com.arturmaslov.lumic.utils.Constants.SENSITIVITY_THRESHOLD_INITIAL
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import kotlin.getValue

class CameraUtils(
    context: Context,
    private val sensitivitySettingsCache: SensitivitySettingCache
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

            val dataNotEmpty = dataSnapshot.isNotEmpty()
            val currentSensitivityThreshold = sensitivitySettingsCache.get() ?: SENSITIVITY_THRESHOLD_INITIAL
            val highAmplitude = dataSnapshot.any { it > currentSensitivityThreshold }

            if (dataNotEmpty && highAmplitude) {
                cameraManager.setTorchMode(cameraId, true) // Turn on
                timesFlashed.value++
                delay(Constants.FLASH_ON_DURATION_MS)
                cameraManager.setTorchMode(cameraId, false) // Turn off
            }
        }
    }

    fun timesFlashed() = timesFlashed as StateFlow<Int>
    fun hasFlash() = hasFlash as StateFlow<Boolean>
}