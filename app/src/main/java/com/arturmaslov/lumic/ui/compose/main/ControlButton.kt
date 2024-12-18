package com.arturmaslov.lumic.ui.compose.main

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.arturmaslov.lumic.utils.ColorMode
import com.arturmaslov.lumic.utils.Constants
import com.arturmaslov.lumic.utils.modifyColor

@Composable
fun ControlButton(
    modifier: Modifier = Modifier,
    bgTint: Int,
    onControlButtonClick: () -> Unit,
    iconVector: ImageVector,
    contentDescription: String = Constants.EMPTY_STRING,
    size: Dp = 60.dp,
    colorMode: ColorMode = ColorMode.DARKER,
    rippleEnabled: Boolean = true
) {
    IconButton(
        onClick = onControlButtonClick,
        modifier.size(size),
        enabled = rippleEnabled
    ) {
        Icon(
            iconVector,
            contentDescription,
            tint = Color(bgTint.modifyColor(colorMode.value)),
            modifier = Modifier.size(size),
        )
    }
}