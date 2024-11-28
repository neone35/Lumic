package com.arturmaslov.lumic.ui.compose.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.arturmaslov.lumic.ui.theme.LumicTheme
import com.arturmaslov.lumic.utils.Constants
import com.arturmaslov.lumic.utils.Constants.FLASH_ON_DURATION_MS
import kotlinx.coroutines.delay
import com.arturmaslov.lumic.R
import com.arturmaslov.lumic.ui.compose.ColorPickerDialog
import com.arturmaslov.lumic.ui.compose.SettingsDialog
import com.arturmaslov.lumic.utils.ColorMode
import com.arturmaslov.lumic.utils.Constants.COLOR_INITIAL
import com.arturmaslov.lumic.utils.Constants.SENSITIVITY_THRESHOLD_INITIAL
import com.arturmaslov.lumic.utils.modifyColor
import kotlin.Int

@Composable
@Preview(showBackground = true)
fun PreviewMainControl() {
    LumicTheme {
        MainControl(
            modifier = Modifier,
            bgTint = COLOR_INITIAL,
            onColorPickerOpen = { }
        )
    }
}


@Composable
fun MainControl(
    modifier: Modifier,
    bgTint: Int,
    onColorPickerOpen: () -> Unit = {}
) {
    Box(
        modifier.fillMaxWidth()
    ) {
        Icon(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            imageVector = ImageVector.vectorResource(R.drawable.main_controls_shape),
            contentDescription = "Main controls",
            tint = Color(bgTint.modifyColor(0.6f))
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            horizontalArrangement = Arrangement.Center,
        ) {
            ControlButton(
                bgTint = bgTint,
                onControlButtonClick = onColorPickerOpen,
                iconVector = ImageVector.vectorResource(R.drawable.ic_palette),
                contentDescription = "Color picker",
                size = 50.dp,
                colorMode = ColorMode.LIGHTER
            )
        }
    }
}