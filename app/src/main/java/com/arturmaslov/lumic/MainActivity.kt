package com.arturmaslov.lumic

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.arturmaslov.lumic.ui.compose.MainScreen
import com.arturmaslov.lumic.ui.theme.LumicTheme
import com.arturmaslov.lumic.utils.ActivityHelper
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity(), ActivityHelper {

    private lateinit var cameraManager: CameraManager
    private lateinit var cameraId: String
    private var timesFlashed = MutableStateFlow(0)
    private var cameraPermissionStatus = MutableStateFlow(PermissionStatus.DENIED)
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            permissions.entries.forEach {
                cameraPermissionStatus.value = if (it.key == Manifest.permission.CAMERA && it.value) {
                    // Permission granted, proceed with flashlight operations
                    PermissionStatus.GRANTED
                } else {
                    // Permission denied, handle accordingly (e.g., show a message)
                    PermissionStatus.DENIED
                }
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestPermissions()
        setObservers()

        setContent {
            val cameraPermissionStatus = cameraPermissionStatus().collectAsState().value
            val timesFlashed = timesFlashed().collectAsState().value
            val hasFlash = hasFlash(this)

            LumicTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    content = { innerPadding ->
                        MainScreen(
                            modifier = Modifier.padding(innerPadding),
                            cameraPermissionStatus = cameraPermissionStatus,
                            hasFlash = hasFlash,
                            timesFlashed = timesFlashed,
                        )
                    }
                )
            }
        }
    }

    private fun hasFlash(context: Context): Boolean {
        cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
        cameraId = cameraManager.cameraIdList[0] // Get the first camera ID
        val hasFlash = cameraManager.getCameraCharacteristics(cameraId)
            .get(CameraCharacteristics.FLASH_INFO_AVAILABLE) == true
        return hasFlash
    }

    private fun flashLight(flashCount: Int) {
        lifecycleScope.launch {
            repeat(flashCount) {
                cameraManager.setTorchMode(cameraId, true) // Turn on
                delay(500)
                cameraManager.setTorchMode(cameraId, false) // Turn off
                delay(500)
                timesFlashed.value++
            }
        }
    }

    override fun setListeners() {}

    override fun setObservers() {
        // observe permission change
        lifecycleScope.launch {
            cameraPermissionStatus.collect {
                if (it == PermissionStatus.GRANTED && hasFlash(baseContext)) {
                    flashLight(3)
                }
            }
        }
    }

    override fun requestPermissions() {
        // check camera permission
        requestPermissionLauncher.launch(arrayOf(Manifest.permission.CAMERA))
    }

    private fun cameraPermissionStatus() = cameraPermissionStatus as StateFlow<PermissionStatus>
    private fun timesFlashed() = timesFlashed as StateFlow<Int>

    enum class PermissionStatus {
        GRANTED,
        DENIED
    }

    companion object {
    }
}