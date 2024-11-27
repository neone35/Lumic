package com.arturmaslov.lumic.ui.compose.main

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arturmaslov.lumic.R

@Composable
fun MultiChoiceFab(
    bgColor: Color,
    iconColor: Color,
    onFlashModeSelected: (FlashModeState) -> Unit,
    currentFlashModeString: FlashModeState,
    hasFlash: Boolean
) {
    var expanded by remember { mutableStateOf(false) } // State to track expansion
    val rotationAngle by animateFloatAsState(
        targetValue = if (expanded) 45f else 0f,
        label = ""
    ) // Animate rotation
    var currentFlashState by remember { mutableStateOf(currentFlashModeString) }
    val currentIcon = when (currentFlashState) {
        FlashModeState.BOTH -> ImageVector.vectorResource(R.drawable.ic_light_both)
        FlashModeState.FLASH -> ImageVector.vectorResource(R.drawable.ic_light_flash)
        FlashModeState.SCREEN -> ImageVector.vectorResource(R.drawable.ic_light_screen)
        FlashModeState.NONE -> ImageVector.vectorResource(R.drawable.ic_light_none)
        else -> ImageVector.vectorResource(R.drawable.ic_light_both)
    }
    LaunchedEffect(currentFlashState) {
        currentFlashState = currentFlashModeString
    }

    Box(
        contentAlignment = Alignment.BottomEnd
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            // Main FAB
            FloatingActionButton(
                onClick = { expanded = !expanded },
                shape = CircleShape,
                containerColor = bgColor,
                modifier = Modifier.size(56.dp)
            ) {
                Icon(
                    currentIcon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier
                        .rotate(rotationAngle)
                        .size(24.dp)
                )
            }
            // Show options only when expanded
            if (expanded) {
                if (currentFlashState != FlashModeState.BOTH && hasFlash)
                    FabOption(
                        bgColor,
                        iconColor,
                        label = "Both",
                        onClick = {
                            onFlashModeSelected(FlashModeState.BOTH)
                            currentFlashState = FlashModeState.BOTH
                            expanded = false
                        },
                        iconVector = ImageVector.vectorResource(R.drawable.ic_light_both)
                    )
                if (currentFlashState != FlashModeState.FLASH && hasFlash)
                    FabOption(
                        bgColor,
                        iconColor,
                        label = "Flash",
                        onClick = {
                            onFlashModeSelected(FlashModeState.FLASH)
                            currentFlashState = FlashModeState.FLASH
                            expanded = false
                        },
                        iconVector = ImageVector.vectorResource(R.drawable.ic_light_flash)
                    )
                if (currentFlashState != FlashModeState.SCREEN)
                    FabOption(
                        bgColor,
                        iconColor,
                        label = "Screen",
                        onClick = {
                            onFlashModeSelected(FlashModeState.SCREEN)
                            currentFlashState = FlashModeState.SCREEN
                            expanded = false
                        },
                        iconVector = ImageVector.vectorResource(R.drawable.ic_light_screen)
                    )
                if (currentFlashState != FlashModeState.NONE)
                    FabOption(
                        bgColor,
                        iconColor,
                        label = "None",
                        onClick = {
                            onFlashModeSelected(FlashModeState.NONE)
                            currentFlashState = FlashModeState.NONE
                            expanded = false
                        },
                        iconVector = ImageVector.vectorResource(R.drawable.ic_light_none)
                    )
            }
        }
    }
}

@Composable
fun FabOption(
    bgColor: Color,
    iconColor: Color,
    label: String,
    onClick: () -> Unit,
    iconVector: ImageVector,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(end = 12.dp)
    ) {
        // FAB option button
        FloatingActionButton(
            modifier = Modifier
                .size(48.dp),
            onClick = onClick,
            shape = CircleShape,
            containerColor = bgColor
        ) {
            Icon(
                iconVector,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(24.dp)
            )
        }
        Text(
            text = label,
            fontSize = 14.sp,
            modifier = Modifier.padding(start = 8.dp),
            color = bgColor
        )
    }
}

enum class FlashModeState(val stateString: String) {
    BOTH("both"),
    FLASH("flash"),
    SCREEN("screen"),
    NONE("none")
}