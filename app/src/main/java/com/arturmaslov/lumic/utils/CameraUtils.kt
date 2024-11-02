package com.arturmaslov.lumic.utils

import android.content.Context
import android.content.Context.CAMERA_SERVICE
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CameraUtils(private val context: Context) {
    private var cameraManager: CameraManager =
        context.getSystemService(CAMERA_SERVICE) as CameraManager
    private var cameraId: String = cameraManager.cameraIdList[0]

    private var timesFlashed = MutableStateFlow(0)

    fun hasFlash(): Boolean {
        val hasFlash = cameraManager.getCameraCharacteristics(cameraId)
            .get(CameraCharacteristics.FLASH_INFO_AVAILABLE) == true
        return hasFlash
    }

    fun flashLight(volumeData: List<Int>) {
        CoroutineScope(Dispatchers.IO).launch {
            val dataSnapshot = volumeData.toList() // Create a copy of the list to avoid concurrent issue

            val dataNotEmpty = dataSnapshot.isNotEmpty()
            val highAmplitude = dataSnapshot.any { it > Constants.SENSITIVITY_THRESHOLD }

            if (dataNotEmpty && highAmplitude) {
                cameraManager.setTorchMode(cameraId, true) // Turn on
                delay(Constants.FLASH_ON_DURATION_MS)
                cameraManager.setTorchMode(cameraId, false) // Turn off
                delay(Constants.FLASH_ON_DURATION_MS)
                timesFlashed.value++
            }
        }
    }

    fun timesFlashed() = timesFlashed as StateFlow<Int>
}