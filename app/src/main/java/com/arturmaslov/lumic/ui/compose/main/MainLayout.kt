package com.arturmaslov.lumic.ui.compose.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arturmaslov.lumic.R
import com.arturmaslov.lumic.data.UserSetting
import com.arturmaslov.lumic.ui.theme.LumicTheme
import com.arturmaslov.lumic.utils.Constants.FLASH_ON_DURATION_INITIAL
import kotlinx.coroutines.delay
import com.arturmaslov.lumic.ui.compose.ColorPickerDialog
import com.arturmaslov.lumic.ui.compose.SetsDialog
import com.arturmaslov.lumic.ui.compose.effects.ToggleFullScreen
import com.arturmaslov.lumic.ui.compose.SettingsDialog
import com.arturmaslov.lumic.ui.compose.effects.ToggleScreenBrightness
import com.arturmaslov.lumic.utils.ColorMode
import com.arturmaslov.lumic.utils.Constants.COLOR_OFF
import com.arturmaslov.lumic.utils.Constants.SENSITIVITY_THRESHOLD_INITIAL
import com.arturmaslov.lumic.utils.FlashMode
import com.arturmaslov.lumic.utils.getAppName
import com.arturmaslov.lumic.utils.modifyColor
import kotlin.Int

@Composable
@Preview(showBackground = true)
fun PreviewMainScreen() {
    LumicTheme {
        MainScreen(
            cameraAllowed = true,
            audioRecordAllowed = true,
            hasFlash = true,
            timesFlashed = 0,
            onColorSelected = { },
            currentColorSetting = COLOR_OFF,
            onMicrophoneSliderValueSelected = { },
            currentSensitivityThreshold = SENSITIVITY_THRESHOLD_INITIAL,
            onFlashModeSelected = { },
            currentFlashMode = FlashMode.BOTH,
            onStrobeModeChange = { },
            onFlashDurationSliderValueSelected = { },
            currentFlashDuration = FLASH_ON_DURATION_INITIAL.toFloat(),
            callingWindow = null,
            onSetSaved = { },
            onSetActivated = { },
            allUserSettings = emptyList(),
            activeSetId = 0,
            onLocked = { }
        )
    }
}


@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    cameraAllowed: Boolean,
    audioRecordAllowed: Boolean,
    hasFlash: Boolean,
    timesFlashed: Int,
    navToPermissionScreen: () -> Unit = {},
    onColorSelected: (Int) -> Unit = {},
    currentColorSetting: Int,
    onMicrophoneSliderValueSelected: (Float) -> Unit,
    currentSensitivityThreshold: Float,
    onFlashModeSelected: (FlashMode) -> Unit,
    currentFlashMode: FlashMode,
    onStrobeModeChange: () -> Unit = {},
    onFlashDurationSliderValueSelected: (Float) -> Unit,
    currentFlashDuration: Float,
    callingWindow: android.view.Window?,
    onSetSaved: (Int) -> Unit,
    onSetActivated: (Int) -> Unit,
    allUserSettings: List<UserSetting>,
    activeSetId: Int,
    onLocked: (Boolean) -> Unit
) {
    val bgColor = remember { mutableIntStateOf(currentColorSetting) }
    var locked by remember { mutableStateOf(false) } // State to lock (no touch / fullscreen)

    var isColorPickerDialogVisible by remember { mutableStateOf(false) }
    var isSettingsDialogVisible by remember { mutableStateOf(false) }
    var isSetsDialogVisible by remember { mutableStateOf(false) }

    val bothOrScreenFlashMode = when (currentFlashMode) {
        FlashMode.SCREEN -> true
        FlashMode.BOTH -> true
        else -> false
    }
    LaunchedEffect(timesFlashed) { // Restart the effect when the timesFlashed changes
        if (bothOrScreenFlashMode) {
            val tempColor = currentColorSetting
            if (timesFlashed > 0) {
                bgColor.intValue = COLOR_OFF
                delay(currentFlashDuration.toLong())
                bgColor.intValue = tempColor
            }
        }
    }
    LaunchedEffect(currentColorSetting) { //changed outside, redraw
        bgColor.intValue = currentColorSetting
    }
    LaunchedEffect(currentFlashMode) {
        if (bothOrScreenFlashMode) {
            bgColor.intValue = currentColorSetting
        }
    }
    callingWindow?.let {
        if (locked) {
            ToggleFullScreen(window = callingWindow, enabled = true)
            ToggleScreenBrightness(window = callingWindow, isMaxBrightness = true)
        } else {
            ToggleFullScreen(window = callingWindow, enabled = false)
            ToggleScreenBrightness(window = callingWindow, isMaxBrightness = false)
        }
    }

    if (cameraAllowed && audioRecordAllowed) {
        val finalColor = if (!bothOrScreenFlashMode) {
            bgColor.intValue = COLOR_OFF
            Color(COLOR_OFF)
        } else {
            Color(bgColor.intValue)
        }
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(finalColor)
        ) {
            val darkerColor = Color(bgColor.intValue.modifyColor(ColorMode.DARKER.value))

            // top UI with name and lock/unlock below
            Column(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = LocalContext.current.getAppName().lowercase(),
                    color = darkerColor,
                    fontSize = 26.sp
                )
                // top end lock switch
                val lockIcon = if (locked) {
                    ImageVector.vectorResource(R.drawable.ic_lock_open)
                } else {
                    ImageVector.vectorResource(R.drawable.ic_lock)
                }
                ControlButton(
                    modifier = Modifier
                        .padding(top = 16.dp),
                    bgTint = bgColor.intValue,
                    onControlButtonClick = {
                        locked = !locked
                        onLocked(locked)
                    },
                    iconVector = lockIcon,
                    contentDescription = stringResource(R.string.lock_or_unlock),
                    size = 50.dp
                )
            }


            // center main control
            if (currentFlashMode != FlashMode.STROBE) {
                MainControl(
                    modifier = Modifier.align(Alignment.Center),
                    bgTint = bgColor.intValue,
                    onColorPickerOpen = { isColorPickerDialogVisible = true },
                    onSettingsOpen = { isSettingsDialogVisible = true },
                    onSetsOpen = { isSetsDialogVisible = true },
                    currentFlashMode = currentFlashMode,
                    onFlashModeSelected = onFlashModeSelected
                )
            }

            // bottom controls
            BottomControls(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .align(Alignment.BottomCenter)
                    .wrapContentHeight(),
                multiChoiceColor = darkerColor,
                iconColor = bgColor.intValue,
                onFlashModeSelected = onFlashModeSelected,
                currentFlashMode = currentFlashMode,
                onStrobeModeChange = onStrobeModeChange,
                hasFlash = hasFlash
            )

            LockedOverlay(locked)

        }
        if (isColorPickerDialogVisible) {
            ColorPickerDialog(
                bgColor = bgColor,
                onColorPickerDismiss = { isColorPickerDialogVisible = false },
                onColorSelected = onColorSelected
            )
        }
        if (isSettingsDialogVisible) {
            SettingsDialog(
                onSettingsDismiss = { isSettingsDialogVisible = false },
                onMicrophoneSliderValueSelected = onMicrophoneSliderValueSelected,
                currentSensitivityThreshold = currentSensitivityThreshold,
                onFlashDurationSliderValueSelected = onFlashDurationSliderValueSelected,
                currentFlashDuration = currentFlashDuration
            )
        }
        if (isSetsDialogVisible) {
            SetsDialog(
                onSetSaved = onSetSaved,
                onSetActivated = onSetActivated,
                onSetsDismiss = { isSetsDialogVisible = false },
                allUserSettings = allUserSettings,
                activeSetId = activeSetId
            )
        }
    } else {
        navToPermissionScreen()
    }
}

@Composable
fun BottomControls(
    modifier: Modifier = Modifier,
    multiChoiceColor: Color,
    iconColor: Int,
    onFlashModeSelected: (FlashMode) -> Unit = {},
    currentFlashMode: FlashMode = FlashMode.BOTH,
    onStrobeModeChange: () -> Unit = {},
    hasFlash: Boolean
) {
    Box(
        modifier
    ) {
        MultiChoiceFab(
            modifier = Modifier.align(Alignment.BottomStart),
            bgColor = multiChoiceColor,
            iconColor = Color(iconColor),
            onFlashModeSelected = onFlashModeSelected,
            currentFlashMode = currentFlashMode,
            hasFlash = hasFlash
        )
        val strobeIcon = if (currentFlashMode == FlashMode.STROBE) {
            ImageVector.vectorResource(R.drawable.ic_strobe_on)
        } else {
            ImageVector.vectorResource(R.drawable.ic_strobe_off)
        }
        if (hasFlash) {
            ControlButton(
                modifier = Modifier.align(Alignment.BottomEnd),
                bgTint = iconColor,
                onControlButtonClick = onStrobeModeChange,
                iconVector = strobeIcon,
                contentDescription = stringResource(R.string.strobe_mode),
                size = 50.dp
            )
        }
    }
}

@Composable
fun LockedOverlay(
    locked: Boolean
) {
    if (locked) {
        Box(
            modifier = Modifier
                .padding(top = 100.dp) // make space for unlock to be touchable
                .fillMaxSize()
                .pointerInput(Unit) { // Intercept all touch gestures
                    awaitPointerEventScope {
                        while (true) {
                            awaitPointerEvent() // Block all events
                        }
                    }
                }
        )
    }
}