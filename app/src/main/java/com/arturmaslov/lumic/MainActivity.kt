package com.arturmaslov.lumic

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.arturmaslov.lumic.ui.compose.LoadingScreen
import com.arturmaslov.lumic.ui.compose.main.MainScreen
import com.arturmaslov.lumic.ui.compose.PermissionAskScreen
import com.arturmaslov.lumic.utils.FlashMode
import com.arturmaslov.lumic.ui.theme.LumicTheme
import com.arturmaslov.lumic.utils.ActivityHelper
import com.arturmaslov.lumic.utils.AudioUtils
import com.arturmaslov.lumic.utils.CameraUtils
import com.arturmaslov.lumic.utils.Constants
import com.arturmaslov.lumic.utils.Constants.COLOR_INITIAL
import com.arturmaslov.lumic.utils.Constants.SENSITIVITY_THRESHOLD_INITIAL
import com.arturmaslov.lumic.utils.LoadStatus
import com.arturmaslov.lumic.utils.PermissionStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MainActivity : BaseActivity(), ActivityHelper {

    val cameraUtils by inject<CameraUtils>()
    val audioUtils by inject<AudioUtils>()

    private var recordFlashJob: Job? = null

    fun checkPermissions() {
        mapOf(
            baseCameraPermissionStatus to Manifest.permission.CAMERA,
            baseAudioRecordPermissionStatus to Manifest.permission.RECORD_AUDIO
        ).forEach { (status, permission) ->
            if (ContextCompat.checkSelfPermission(
                    this,
                    permission
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                status.value = PermissionStatus.GRANTED
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        checkPermissions()
        setObservers()

        setContent {
            val navController = rememberNavController()
            LocalOnBackPressedDispatcherOwner provides this

            val cameraPermissionStatus = cameraPermissionStatus().collectAsState().value
            val audioRecordPermissionStatus = audioRecordPermissionStatus().collectAsState().value
            val timesFlashed = cameraUtils.timesFlashed().collectAsState().value
            val hasFlash = cameraUtils.hasFlash().collectAsState().value
            val loadStatusState = loadStatus().collectAsState().value

            var isColorPickerDialogVisible by remember { mutableStateOf(false) }
            var colorSetting by remember { mutableIntStateOf(COLOR_INITIAL) }
            LaunchedEffect(key1 = true) {
                colorSetting = colorSettingsCache.get() ?: COLOR_INITIAL
            }
            var isSettingsDialogVisible by remember { mutableStateOf(false) }
            var sensitivityThreshold by remember { mutableFloatStateOf(SENSITIVITY_THRESHOLD_INITIAL) }
            LaunchedEffect(key1 = true) {
                sensitivityThreshold =
                    sensitivitySettingsCache.get() ?: SENSITIVITY_THRESHOLD_INITIAL
            }
            var flashMode by remember { mutableStateOf(FlashMode.BOTH) }
            LaunchedEffect(key1 = true) {
                flashMode = flashSettingsCache.get() ?: FlashMode.BOTH
            }

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
                                        modifier = Modifier
                                            .padding(innerPadding),
                                        cameraAllowed = cameraPermissionStatus.bool,
                                        audioRecordAllowed = audioRecordPermissionStatus.bool,
                                        hasFlash = hasFlash,
                                        timesFlashed = timesFlashed,
                                        navToPermissionScreen = {
                                            navController.navigate(PERMISSION_SCREEN)
                                        },
                                        isColorPickerDialogVisible = isColorPickerDialogVisible,
                                        onColorPickerOpen = { isColorPickerDialogVisible = true },
                                        onColorPickerDismiss = {
                                            isColorPickerDialogVisible = false
                                        },
                                        onColorSelected = { color ->
                                            CoroutineScope(Dispatchers.IO).launch {
                                                colorSettingsCache.set(color)
                                                colorSetting = color
                                            }
                                        },
                                        currentColorSetting = colorSetting,
                                        isSettingsDialogVisible = isSettingsDialogVisible,
                                        onSettingsOpen = { isSettingsDialogVisible = true },
                                        onSettingsDismiss = { isSettingsDialogVisible = false },
                                        onMicrophoneSliderValueSelected = { value ->
                                            CoroutineScope(Dispatchers.IO).launch {
                                                sensitivitySettingsCache.set(value)
                                                sensitivityThreshold = value
                                            }
                                        },
                                        currentSensitivityThreshold = sensitivityThreshold,
                                        onFlashModeSelected = { value ->
                                            CoroutineScope(Dispatchers.IO).launch {
                                                flashSettingsCache.set(value)
                                                flashMode = value
                                            }
                                        },
                                        currentFlashMode = flashMode,
                                        onStrobeModeChange = {
                                            CoroutineScope(Dispatchers.IO).launch {
                                                if (flashMode != FlashMode.STROBE) {
                                                    flashSettingsCache.set(FlashMode.STROBE)
                                                    flashMode = FlashMode.STROBE
                                                } else {
                                                    flashSettingsCache.set(FlashMode.BOTH)
                                                    flashMode = FlashMode.BOTH
                                                }
                                            }
                                        }
                                    )
                                }
                                composable(PERMISSION_SCREEN) {
                                    PermissionAskScreen(
                                        cameraAllowed = cameraPermissionStatus.bool,
                                        audioRecordAllowed = audioRecordPermissionStatus.bool,
                                        onRequestPermissions = {
                                            requestPermissions(
                                                arrayOf(
                                                    Manifest.permission.CAMERA,
                                                    Manifest.permission.RECORD_AUDIO
                                                )
                                            )
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
                cameraUtils.checkIfHasFlash()
                val currentFlashMode = flashSettingsCache.get()
                val notNoneFlashMode = when (currentFlashMode) {
                    FlashMode.SCREEN -> true
                    FlashMode.BOTH -> true
                    FlashMode.FLASH -> true
                    FlashMode.STROBE -> true
                    else -> false
                }
                if (bothPermissionsGranted && notNoneFlashMode) {
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