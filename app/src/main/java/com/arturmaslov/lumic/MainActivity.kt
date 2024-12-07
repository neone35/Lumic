package com.arturmaslov.lumic

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
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
import com.arturmaslov.lumic.data.UserSetting
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
import com.arturmaslov.lumic.utils.Constants.FLASH_ON_DURATION_INITIAL
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

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Lock to portrait mode
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

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

            // get currently active settings
            var currentActiveUserId by remember { mutableIntStateOf(1) }
            var currentActiveSettingId by remember { mutableIntStateOf(1) }
            var currentActiveSettingSet by remember { mutableStateOf(UserSetting()) }
            var allUserSettingSets by remember { mutableStateOf(listOf<UserSetting>()) }
            LaunchedEffect(currentActiveSettingSet) {
                currentActiveUserId = activeUserSettingsCache.get()?.first ?: 1
                currentActiveSettingId = activeUserSettingsCache.get()?.second ?: 1

                val activeUserIsSet = currentActiveUserId != 0
                val activeSettingIdIsSet = currentActiveSettingId != 0
                if (activeUserIsSet && activeSettingIdIsSet) {
                    allUserSettingSets = mainRepository.getOneUserSettings(currentActiveUserId) ?: emptyList()
                    val oneSetting = allUserSettingSets.find { it.id == currentActiveSettingId }
                    if (oneSetting != null) {
                        currentActiveSettingSet = oneSetting
                    }
                }
            }
            val activeColor = currentActiveSettingSet.colorSetting
            val activeFlashDuration = currentActiveSettingSet.flashDuration
            val activeFlashMode = currentActiveSettingSet.flashMode
            val activeSensitivity = currentActiveSettingSet.sensitivity

            // load active settings and hold live changes in state memory
            var colorSetting by remember { mutableIntStateOf(COLOR_INITIAL) }
            LaunchedEffect(activeColor) {
                colorSetting = activeColor ?: colorSettingsCache.get() ?: COLOR_INITIAL
            }
            var sensitivityThreshold by remember { mutableFloatStateOf(SENSITIVITY_THRESHOLD_INITIAL) }
            LaunchedEffect(activeSensitivity) {
                sensitivityThreshold =
                    activeSensitivity ?: sensitivitySettingsCache.get() ?: SENSITIVITY_THRESHOLD_INITIAL
            }
            var flashMode = baseFlashMode.collectAsState().value
            LaunchedEffect(activeFlashMode) {
                baseFlashMode.value = activeFlashMode ?: flashSettingsCache.get() ?: FlashMode.BOTH
            }
            var flashDuration by remember { mutableFloatStateOf(FLASH_ON_DURATION_INITIAL) }
            LaunchedEffect(activeFlashDuration) {
                flashDuration =
                    activeFlashDuration ?: flashDurationSettingsCache.get() ?: FLASH_ON_DURATION_INITIAL
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
                                        onColorSelected = { color ->
                                            CoroutineScope(Dispatchers.IO).launch {
                                                colorSettingsCache.set(color)
                                                colorSetting = color
                                            }
                                        },
                                        currentColorSetting = colorSetting,
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
                                                baseFlashMode.value = value
                                            }
                                        },
                                        currentFlashMode = flashMode,
                                        onStrobeModeChange = {
                                            CoroutineScope(Dispatchers.IO).launch {
                                                if (flashMode != FlashMode.STROBE) {
                                                    flashSettingsCache.set(FlashMode.STROBE)
                                                    baseFlashMode.value = FlashMode.STROBE
                                                } else {
                                                    flashSettingsCache.set(FlashMode.BOTH)
                                                    baseFlashMode.value = FlashMode.BOTH
                                                }
                                            }
                                        },
                                        onFlashDurationSliderValueSelected = { value ->
                                            CoroutineScope(Dispatchers.IO).launch {
                                                flashDurationSettingsCache.set(value)
                                                flashDuration = value
                                            }
                                        },
                                        currentFlashDuration = flashDuration,
                                        callingWindow = window,
                                        onSetSaved = { userSettingId ->
                                            CoroutineScope(Dispatchers.IO).launch {
                                                val userSettingToInsert = UserSetting(
                                                    id = userSettingId,
                                                    userId = currentActiveUserId,
                                                    colorSetting = colorSetting,
                                                    flashDuration = flashDuration,
                                                    flashMode = flashMode,
                                                    sensitivity = sensitivityThreshold
                                                )
                                                mainRepository.insertUserSetting(userSettingToInsert)
                                            }
                                        },
                                        onSetActivated = { userSettingId ->
                                            CoroutineScope(Dispatchers.IO).launch {
                                                val userSettingToActivate = mainRepository.getUserSetting(userSettingId)
                                                activeUserSettingsCache.set(Pair(
                                                    userSettingToActivate?.userId ?: currentActiveUserId,
                                                    userSettingToActivate?.id ?: currentActiveSettingId
                                                ))
                                                currentActiveSettingSet = userSettingToActivate ?: UserSetting()
                                                currentActiveSettingId = userSettingToActivate?.id ?: 0

                                            }
                                        },
                                        allUserSettings = allUserSettingSets,
                                        activeSetId = currentActiveSettingId
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
            baseFlashMode.collect {
                while (isActive) {
                    val notStrobe = baseFlashMode.value != FlashMode.STROBE
                    if (notStrobe) audioUtils.startRecording()
                    if (notStrobe) delay(Constants.AUDIO_RECORD_INTERVAL_MS)
                    cameraUtils.flashLight(audioUtils.volumeData)
                    delay(Constants.DELAY_FROM_FLASH_TO_STOP_MS)
                    audioUtils.stopRecording() // needed on switch to strobe
                }
            }
        }
    }

    override fun requestPermissions(permissionArray: Array<String>) {
        baseRequestPermissionLauncher.launch(permissionArray)
    }

    override fun onStop() {
        recordFlashJob?.cancel()
        audioUtils.stopRecording()
        super.onStop()
    }

    companion object {
        const val MAIN_SCREEN = "main_screen"
        const val PERMISSION_SCREEN = "permission_screen"
    }
}