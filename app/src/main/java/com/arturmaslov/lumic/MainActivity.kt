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
import com.arturmaslov.lumic.utils.CameraUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity(), ActivityHelper {

    private lateinit var cameraManager: CameraManager
    private lateinit var cameraId: String
    private var hasFlash = false

    private val cameraPermissionStatus = MutableStateFlow(PermissionStatus.DENIED)
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val permissionKeys = permissions.keys.map { it }
            permissions.entries.forEachIndexed { index, entry ->
                cameraPermissionStatus.value = if (entry.key == permissionKeys[index] && entry.value) {
                    // Permission granted, proceed with flashlight operations
                    PermissionStatus.GRANTED
                } else {
                    // Permission denied, handle accordingly (e.g., show a message)
                    PermissionStatus.DENIED
                }
            }
        }
    private lateinit var cameraUtils: CameraUtils


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestPermissions(arrayOf(Manifest.permission.CAMERA))
        setObservers()
    }

    override fun setListeners() {}

    override fun setObservers() {
        // observe permission change
        lifecycleScope.launch {
            cameraPermissionStatus.collect {
                if (it == PermissionStatus.GRANTED) {
                    cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
                    cameraId = cameraManager.cameraIdList[0] // Get the first camera ID
                    cameraUtils = CameraUtils(cameraManager, cameraId)
                    hasFlash = cameraUtils.hasFlash(baseContext)
                    if (hasFlash) {
                        cameraUtils.flashLight(3)
                    }

                    setContent {
                        val cameraPermissionStatus = cameraPermissionStatus().collectAsState().value
                        val timesFlashed = cameraUtils.timesFlashed().collectAsState().value

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
            }
        }
    }

    override fun requestPermissions(permissionArray: Array<String>) {
        requestPermissionLauncher.launch(permissionArray)
    }

    private fun cameraPermissionStatus() = cameraPermissionStatus as StateFlow<PermissionStatus>

    enum class PermissionStatus {
        GRANTED,
        DENIED
    }

    companion object {
    }
}