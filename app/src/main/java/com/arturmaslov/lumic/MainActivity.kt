package com.arturmaslov.lumic

import android.Manifest
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.arturmaslov.lumic.MainActivity.PermissionStatus
import com.arturmaslov.lumic.ui.compose.MainScreen
import com.arturmaslov.lumic.ui.theme.LumicTheme
import com.arturmaslov.lumic.utils.ActivityHelper
import com.arturmaslov.lumic.utils.AudioUtils
import com.arturmaslov.lumic.utils.CameraUtils
import com.arturmaslov.lumic.utils.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity(), ActivityHelper {

    private val cameraPermissionStatus = MutableStateFlow(PermissionStatus.DENIED)
    private val audioRecordPermissionStatus = MutableStateFlow(PermissionStatus.DENIED)
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
//            val permissionKeys = permissions.keys.map { it }
            permissions.entries.forEachIndexed { index, entry ->
                when (entry.key) {
                    Manifest.permission.CAMERA -> {
                        cameraPermissionStatus.value =
                            if (entry.value) {
                                PermissionStatus.GRANTED
                            } else {
                                PermissionStatus.DENIED
                            }
                    }

                    Manifest.permission.RECORD_AUDIO -> {
                        audioRecordPermissionStatus.value =
                            if (entry.value) {
                                PermissionStatus.GRANTED
                            } else {
                                PermissionStatus.DENIED
                            }
                    }
                }
            }
        }
    private lateinit var cameraUtils: CameraUtils
    private lateinit var audioUtils: AudioUtils

    private var recordFlashJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestPermissions(arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO))
        cameraUtils = CameraUtils(this@MainActivity)
        audioUtils = AudioUtils(this@MainActivity)
        setObservers()

        setContent {
            val cameraPermissionStatus = cameraPermissionStatus().collectAsState().value
            val audioRecordPermissionStatus = audioRecordPermissionStatus().collectAsState().value
            val timesFlashed = cameraUtils.timesFlashed().collectAsState().value

            LumicTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    content = { innerPadding ->
                        MainScreen(
                            modifier = Modifier.padding(innerPadding),
                            cameraPermissionStatus = cameraPermissionStatus,
                            audioRecordPermissionStatus = audioRecordPermissionStatus,
                            hasFlash = cameraUtils.hasFlash(),
                            timesFlashed = timesFlashed,
                        )
                    }
                )
            }
        }
    }

    override fun setListeners() {}

    override fun setObservers() {
        lifecycleScope.launch {
            // Combine both flows
            combine(cameraPermissionStatus, audioRecordPermissionStatus)
            { cameraPermission, audioPermission ->
                cameraPermission == PermissionStatus.GRANTED
                        && audioPermission == PermissionStatus.GRANTED
            }.collect { bothPermissionsGranted ->
                if (bothPermissionsGranted) {
                    launchRecordFlashing()
                }
            }
        }
    }

    private fun launchRecordFlashing() {
        recordFlashJob = CoroutineScope(Dispatchers.IO).launch {
            while (isActive) {
                audioUtils.startRecording()
                delay(Constants.AUDIO_RECORD_INTERVAL_MS)
                cameraUtils.flashLight(audioUtils.volumeData)
                delay(Constants.DELAY_FROM_FLASH_TO_STOP_MS)
                audioUtils.stopRecording()
            }
        }
    }

    override fun requestPermissions(permissionArray: Array<String>) {
        requestPermissionLauncher.launch(permissionArray)
    }

    override fun onDestroy() {
        super.onDestroy()
        recordFlashJob?.cancel()
        audioUtils.stopRecording()
    }

    private fun cameraPermissionStatus() = cameraPermissionStatus as StateFlow<PermissionStatus>
    private fun audioRecordPermissionStatus() =
        audioRecordPermissionStatus as StateFlow<PermissionStatus>

    enum class PermissionStatus {
        GRANTED,
        DENIED
    }

    companion object {
    }
}