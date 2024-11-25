package com.arturmaslov.lumic.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.arturmaslov.lumic.ui.theme.LumicTheme
import com.arturmaslov.lumic.utils.Constants
import com.arturmaslov.lumic.utils.Constants.FLASH_ON_DURATION_MS
import kotlinx.coroutines.delay
import com.arturmaslov.lumic.R
import com.arturmaslov.lumic.utils.Constants.COLOR_INITIAL
import com.arturmaslov.lumic.utils.Constants.SENSITIVITY_THRESHOLD_INITIAL
import com.github.skydoves.colorpicker.compose.ColorEnvelope
import com.github.skydoves.colorpicker.compose.ColorPickerController
import com.github.skydoves.colorpicker.compose.HsvColorPicker

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
            currentSensitivityThreshold = SENSITIVITY_THRESHOLD_INITIAL
        )
    }
}


@Composable
fun MainScreen(
    modifier: Modifier = Modifier.fillMaxSize(),
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
    currentSensitivityThreshold: Float
) {
    val bgColor = remember { mutableIntStateOf(currentColorSetting) }
    LaunchedEffect(timesFlashed) { // Restart the effect when the timesFlashed changes
        val tempColor = currentColorSetting
        if (timesFlashed > 0) {
            bgColor.value = Color.Black.toArgb()
            delay(FLASH_ON_DURATION_MS)
            bgColor.value = tempColor
        }
    }
    LaunchedEffect(currentColorSetting) { //changed outside, redraw
        bgColor.value = currentColorSetting
    }

    if (cameraAllowed && audioRecordAllowed) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(bgColor.value)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Times Flashed: $timesFlashed")
            if (!hasFlash) {
                Text(text = "Camera has no flashlight")
            }
            ColorPickerButton { onColorPickerOpen() }
            SettingsButton { onSettingsOpen() }
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

@Composable
fun ColorPickerButton(
    onColorPickerOpen: () -> Unit
) {
    IconButton(
        onClick = onColorPickerOpen
    ) {
        Icon(
            ImageVector.vectorResource(R.drawable.ic_palette),
            contentDescription = Constants.EMPTY_STRING,
            tint = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.size(48.dp),
        )
    }
}

@Composable
fun SettingsButton(
    onSettingsOpen: () -> Unit
) {
    IconButton(
        onClick = onSettingsOpen
    ) {
        Icon(
            Icons.Filled.Settings,
            contentDescription = Constants.EMPTY_STRING,
            tint = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.size(48.dp),
        )
    }
}
