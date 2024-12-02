package com.arturmaslov.lumic.ui.compose.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arturmaslov.lumic.R
import com.arturmaslov.lumic.ui.theme.LumicTheme
import com.arturmaslov.lumic.utils.Constants.FLASH_ON_DURATION_INITIAL
import kotlinx.coroutines.delay
import com.arturmaslov.lumic.ui.compose.ColorPickerDialog
import com.arturmaslov.lumic.ui.compose.EnableFullScreen
import com.arturmaslov.lumic.ui.compose.SettingsDialog
import com.arturmaslov.lumic.utils.ColorMode
import com.arturmaslov.lumic.utils.Constants.COLOR_INITIAL
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
            isColorPickerDialogVisible = false,
            isSettingsDialogVisible = false,
            onColorPickerOpen = { },
            onColorPickerDismiss = { },
            onColorSelected = { },
            currentColorSetting = COLOR_INITIAL,
            onSettingsOpen = { },
            onSettingsDismiss = { },
            onMicrophoneSliderValueSelected = { },
            currentSensitivityThreshold = SENSITIVITY_THRESHOLD_INITIAL,
            onFlashModeSelected = { },
            currentFlashMode = FlashMode.BOTH,
            onStrobeModeChange = { },
            onFlashDurationSliderValueSelected = { },
            currentFlashDuration = FLASH_ON_DURATION_INITIAL.toFloat()
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
    isColorPickerDialogVisible: Boolean,
    navToPermissionScreen: () -> Unit = {},
    onColorPickerOpen: () -> Unit = {},
    onColorPickerDismiss: () -> Unit = {},
    onColorSelected: (Int) -> Unit = {},
    currentColorSetting: Int,
    isSettingsDialogVisible: Boolean,
    onSettingsOpen: () -> Unit = {},
    onSettingsDismiss: () -> Unit = {},
    onMicrophoneSliderValueSelected: (Float) -> Unit,
    currentSensitivityThreshold: Float,
    onFlashModeSelected: (FlashMode) -> Unit,
    currentFlashMode: FlashMode,
    onStrobeModeChange: () -> Unit = {},
    onFlashDurationSliderValueSelected: (Float) -> Unit,
    currentFlashDuration: Float
) {
    val bgColor = remember { mutableIntStateOf(currentColorSetting) }

    val bothOrScreenFlashMode = when (currentFlashMode) {
        FlashMode.SCREEN -> true
        FlashMode.BOTH -> true
        else -> false
    }
    LaunchedEffect(timesFlashed) { // Restart the effect when the timesFlashed changes
        if (bothOrScreenFlashMode) {
            val tempColor = currentColorSetting
            if (timesFlashed > 0) {
                bgColor.intValue = COLOR_INITIAL
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

    if (cameraAllowed && audioRecordAllowed) {
        val finalColor = if (!bothOrScreenFlashMode) {
            bgColor.intValue = COLOR_INITIAL
            Color(COLOR_INITIAL)
        } else {
            Color(bgColor.intValue)
        }
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(finalColor)
        ) {
            val darkerColor = Color(bgColor.intValue.modifyColor(ColorMode.DARKER.value))

            // top app name
            Text(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 16.dp),
                text = LocalContext.current.getAppName().lowercase(),
                color = darkerColor,
                fontSize = 26.sp
            )

            // center main control
            if (currentFlashMode != FlashMode.STROBE) {
                MainControl(
                    modifier = Modifier.align(Alignment.Center),
                    bgTint = bgColor.intValue,
                    onColorPickerOpen = onColorPickerOpen,
                    onSettingsOpen = onSettingsOpen,
                    currentFlashMode = currentFlashMode,
                    onFlashModeSelected = onFlashModeSelected
                )
            }

            // bottom controls
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .align(Alignment.BottomCenter)
                    .wrapContentHeight()
            ) {
                MultiChoiceFab(
                    modifier = Modifier.align(Alignment.BottomStart),
                    bgColor = darkerColor,
                    iconColor = Color(bgColor.intValue),
                    onFlashModeSelected = onFlashModeSelected,
                    currentFlashMode = currentFlashMode,
                    hasFlash = hasFlash
                )
                val strobeIcon = if (currentFlashMode == FlashMode.STROBE) {
                    ImageVector.vectorResource(R.drawable.ic_strobe_on)
                } else {
                    ImageVector.vectorResource(R.drawable.ic_strobe_off)
                }
                ControlButton(
                    modifier = Modifier.align(Alignment.BottomEnd),
                    bgTint = bgColor.intValue,
                    onControlButtonClick = onStrobeModeChange,
                    iconVector = strobeIcon,
                    contentDescription = stringResource(R.string.strobe_mode),
                    size = 50.dp
                )
            }

        }
        if (isColorPickerDialogVisible) {
            ColorPickerDialog(
                bgColor = bgColor,
                onColorPickerDismiss = onColorPickerDismiss,
                onColorSelected = onColorSelected
            )
        }
        if (isSettingsDialogVisible) {
            SettingsDialog(
                onSettingsDismiss = onSettingsDismiss,
                onMicrophoneSliderValueSelected = onMicrophoneSliderValueSelected,
                currentSensitivityThreshold = currentSensitivityThreshold,
                onFlashDurationSliderValueSelected = onFlashDurationSliderValueSelected,
                currentFlashDuration = currentFlashDuration
            )
        }
    } else {
        navToPermissionScreen()
    }
}