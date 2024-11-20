package com.arturmaslov.lumic

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.arturmaslov.lumic.ui.compose.LoadingScreen
import com.arturmaslov.lumic.ui.compose.MainScreen
import com.arturmaslov.lumic.ui.compose.PermissionAskScreen
import com.arturmaslov.lumic.ui.theme.LumicTheme
import com.arturmaslov.lumic.utils.ActivityHelper
import com.arturmaslov.lumic.utils.AudioUtils
import com.arturmaslov.lumic.utils.CameraUtils
import com.arturmaslov.lumic.utils.Constants
import com.arturmaslov.lumic.utils.LoadStatus
import com.arturmaslov.lumic.utils.PermissionStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class MainActivity : BaseActivity(), ActivityHelper {

    private lateinit var cameraUtils: CameraUtils
    private lateinit var audioUtils: AudioUtils

    private var recordFlashJob: Job? = null

    fun checkPermissions() {
        mapOf(
            baseCameraPermissionStatus to Manifest.permission.CAMERA,
            baseAudioRecordPermissionStatus to Manifest.permission.RECORD_AUDIO
        ).forEach { (status, permission) ->
            if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
                status.value = PermissionStatus.GRANTED
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        checkPermissions()
        cameraUtils = CameraUtils(this@MainActivity)
        audioUtils = AudioUtils(this@MainActivity)
        setObservers()

        setContent {
            val navController = rememberNavController()
            LocalOnBackPressedDispatcherOwner provides this

            val cameraPermissionStatus = cameraPermissionStatus().collectAsState().value
            val audioRecordPermissionStatus = audioRecordPermissionStatus().collectAsState().value
            val timesFlashed = cameraUtils.timesFlashed().collectAsState().value
            val hasFlash = cameraUtils.hasFlash().collectAsState().value
            val loadStatusState = loadStatus().collectAsState().value

            LumicTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    content = { innerPadding ->
                        LoadingScreen(
                            showLoading = loadStatusState == LoadStatus.DONE
                        ) {
                            NavHost(
                                navController = navController,
                                startDestination = MAIN_SCREEN
                            ) {
                                composable(MAIN_SCREEN) {
                                    MainScreen(
                                        modifier = Modifier.padding(innerPadding),
                                        cameraAllowed = cameraPermissionStatus.bool,
                                        audioRecordAllowed = audioRecordPermissionStatus.bool,
                                        hasFlash = hasFlash,
                                        timesFlashed = timesFlashed,
                                        navToPermissionScreen = {
                                            navController.navigate(PERMISSION_SCREEN)
                                        }
                                    )
                                }
                                composable(PERMISSION_SCREEN) {
                                    PermissionAskScreen(
                                        cameraPermissionStatus = cameraPermissionStatus,
                                        audioRecordPermissionStatus = audioRecordPermissionStatus,
                                        onRequestPermissions = {
                                            requestPermissions(arrayOf(
                                                Manifest.permission.CAMERA,
                                                Manifest.permission.RECORD_AUDIO))
                                        },
                                        navToMainScreen = {
                                            navController.navigate(MAIN_SCREEN)
                                        }
                                    )
                                }
                            }
                        }
                    }
                )
            }
        }
    }

    override fun setListeners() {}

    override fun setObservers() {
        lifecycleScope.launch {
            // Combine both flows
            combine(baseCameraPermissionStatus, baseAudioRecordPermissionStatus)
            { cameraPermission, audioPermission ->
                cameraPermission == PermissionStatus.GRANTED
                        && audioPermission == PermissionStatus.GRANTED
            }.collect { bothPermissionsGranted ->
                if (bothPermissionsGranted) {
                    cameraUtils.checkIfHasFlash()
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
        baseRequestPermissionLauncher.launch(permissionArray)
    }

    override fun onDestroy() {
        super.onDestroy()
        recordFlashJob?.cancel()
        audioUtils.stopRecording()
    }

    companion object {
        const val MAIN_SCREEN = "main_screen"
        const val PERMISSION_SCREEN = "permission_screen"
    }
}