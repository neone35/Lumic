package com.arturmaslov.lumic.utils

import android.content.Context
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CameraUtils(
    private val cameraManager: CameraManager,
    private val cameraId: String
) {
    private var timesFlashed = MutableStateFlow(0)

    fun hasFlash(context: Context): Boolean {
        val hasFlash = cameraManager.getCameraCharacteristics(cameraId)
            .get(CameraCharacteristics.FLASH_INFO_AVAILABLE) == true
        return hasFlash
    }

    fun flashLight(flashCount: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            repeat(flashCount) {
                cameraManager.setTorchMode(cameraId, true) // Turn on
                delay(500)
                cameraManager.setTorchMode(cameraId, false) // Turn off
                delay(500)
                timesFlashed.value++
            }
        }
    }

    fun timesFlashed() = timesFlashed as StateFlow<Int>
}