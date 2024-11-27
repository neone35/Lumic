package com.arturmaslov.lumic.ui.compose.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arturmaslov.lumic.ui.theme.LumicTheme
import com.arturmaslov.lumic.utils.Constants.FLASH_ON_DURATION_MS
import kotlinx.coroutines.delay
import com.arturmaslov.lumic.ui.compose.ColorPickerDialog
import com.arturmaslov.lumic.ui.compose.SettingsDialog
import com.arturmaslov.lumic.utils.Constants.COLOR_INITIAL
import com.arturmaslov.lumic.utils.Constants.SENSITIVITY_THRESHOLD_INITIAL
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
            currentFlashMode = FlashModeState.BOTH
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
    onFlashModeSelected: (FlashModeState) -> Unit,
    currentFlashMode: FlashModeState
) {
    val bgColor = remember { mutableIntStateOf(currentColorSetting) }
    val bothAndScreen = when (currentFlashMode) {
        FlashModeState.NONE -> false
        FlashModeState.SCREEN -> true
        FlashModeState.BOTH -> true
        FlashModeState.FLASH -> false
        else -> false
    }
    LaunchedEffect(timesFlashed) { // Restart the effect when the timesFlashed changes
        if (bothAndScreen) {
            val tempColor = currentColorSetting
            if (timesFlashed > 0) {
                bgColor.intValue = COLOR_INITIAL
                delay(FLASH_ON_DURATION_MS)
                bgColor.intValue = tempColor
            }
        }
    }
    LaunchedEffect(currentColorSetting) { //changed outside, redraw
        bgColor.intValue = currentColorSetting
    }
    LaunchedEffect(currentFlashMode) {
        if (bothAndScreen) {
            bgColor.intValue = currentColorSetting
        }
    }

    if (cameraAllowed && audioRecordAllowed) {
        val finalColor = if (!bothAndScreen) {
            bgColor.intValue = COLOR_INITIAL
            Color(COLOR_INITIAL)
        } else {
            Color(bgColor.intValue)
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(finalColor)
        ) {
            val darkerColor = Color(bgColor.intValue.modifyColor(ColorMode.DARKER.value))

            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .align(Alignment.TopCenter)
                    .wrapContentHeight(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                MultiChoiceFab(
                    bgColor = darkerColor,
                    iconColor = Color(bgColor.intValue),
                    onFlashModeSelected = onFlashModeSelected,
                    currentFlashModeString = currentFlashMode,
                    hasFlash = hasFlash
                )
            }

            MainControl(
                modifier = Modifier.align(Alignment.Center),
                bgTint = bgColor.intValue,
                onColorPickerOpen = onColorPickerOpen
            )

            Text(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(top = 400.dp),
                text = LocalContext.current.getAppName().lowercase(),
                color = darkerColor,
                fontSize = 30.sp
            )

            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .align(Alignment.BottomCenter)
                    .wrapContentHeight(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                ControlButton(
                    bgTint = bgColor.intValue,
                    onControlButtonClick = onSettingsOpen,
                    iconVector = Icons.Filled.Settings,
                    contentDescription = "Settings",
                    size = 60.dp
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
                currentSensitivityThreshold = currentSensitivityThreshold
            )
        }
    } else {
        navToPermissionScreen()
    }
}