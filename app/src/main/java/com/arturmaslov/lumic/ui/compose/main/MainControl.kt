package com.arturmaslov.lumic.ui.compose.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arturmaslov.lumic.ui.theme.LumicTheme
import com.arturmaslov.lumic.R
import com.arturmaslov.lumic.utils.ColorMode
import com.arturmaslov.lumic.utils.Constants.COLOR_INITIAL
import com.arturmaslov.lumic.utils.FlashMode
import com.arturmaslov.lumic.utils.modifyColor
import kotlin.Int

@Composable
@Preview(showBackground = true)
fun PreviewMainControl() {
    LumicTheme {
        MainControl(
            modifier = Modifier,
            bgTint = COLOR_INITIAL,
            onColorPickerOpen = { },
            onSettingsOpen = { },
            currentFlashMode = FlashMode.BOTH,
            onFlashModeSelected = { }
        )
    }
}


@Composable
fun MainControl(
    modifier: Modifier,
    bgTint: Int,
    onColorPickerOpen: () -> Unit = {},
    onSettingsOpen: () -> Unit = {},
    currentFlashMode: FlashMode,
    onFlashModeSelected: (FlashMode) -> Unit,
) {
    val notNoneOrStrobe = when (currentFlashMode) {
        FlashMode.STROBE -> false
        FlashMode.NONE -> false
        else -> true
    }

    Box(
        modifier.fillMaxWidth()
    ) {
        if (notNoneOrStrobe) {
            // background
            Icon(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center),
                imageVector = ImageVector.vectorResource(R.drawable.main_controls_shape),
                contentDescription = stringResource(R.string.main_controls),
                tint = Color(bgTint.modifyColor(ColorMode.DARKER.value))
            )
            // top controls
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)
                    .align(Alignment.TopCenter),
                horizontalArrangement = Arrangement.Center,
            ) {
                ControlButton(
                    bgTint = bgTint,
                    onControlButtonClick = onColorPickerOpen,
                    iconVector = ImageVector.vectorResource(R.drawable.ic_palette),
                    contentDescription = stringResource(R.string.color_picker),
                    size = 50.dp,
                    colorMode = ColorMode.LIGHTER
                )
            }
            // center controls
            Row(
                modifier = Modifier
                    .width(240.dp)
                    .align(Alignment.Center),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                ControlButton(
                    modifier = Modifier.testTag("settings dialog"),
                    bgTint = bgTint,
                    onControlButtonClick = onSettingsOpen,
                    iconVector = Icons.Filled.Settings,
                    contentDescription = stringResource(R.string.settings),
                    size = 50.dp,
                    colorMode = ColorMode.LIGHTER
                )
                ControlButton(
                    bgTint = bgTint,
                    onControlButtonClick = onSettingsOpen,
                    iconVector = Icons.Filled.Settings,
                    contentDescription = stringResource(R.string.settings),
                    size = 50.dp,
                    colorMode = ColorMode.LIGHTER
                )
            }
        }

        // main control (play / stop)
        val startStopIcon = if (notNoneOrStrobe) {
            ImageVector.vectorResource(R.drawable.ic_stop_circle)
        } else {
            ImageVector.vectorResource(R.drawable.ic_play_circle)
        }
        ControlButton(
            modifier = Modifier.align(Alignment.Center),
            bgTint = bgTint,
            onControlButtonClick = {
                if (notNoneOrStrobe) {
                    onFlashModeSelected(FlashMode.NONE)
                } else {
                    onFlashModeSelected(FlashMode.BOTH)
                }
            },
            iconVector = startStopIcon,
            contentDescription = stringResource(R.string.play_stop),
            size = 100.dp,
            colorMode = ColorMode.LIGHTER
        )

        // bottom controls
        if (notNoneOrStrobe) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp)
                    .align(Alignment.BottomCenter),
                horizontalArrangement = Arrangement.Center,
            ) {
                ControlButton(
                    modifier = Modifier.testTag("color picker"),
                    bgTint = bgTint,
                    onControlButtonClick = onColorPickerOpen,
                    iconVector = ImageVector.vectorResource(R.drawable.ic_palette),
                    contentDescription = stringResource(R.string.color_picker),
                    size = 50.dp,
                    colorMode = ColorMode.LIGHTER
                )
            }
        }
    }
}