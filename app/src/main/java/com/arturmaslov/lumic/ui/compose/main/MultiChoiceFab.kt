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
import com.arturmaslov.lumic.utils.Constants
import com.arturmaslov.lumic.utils.FlashMode

@Composable
fun MultiChoiceFab(
    modifier: Modifier = Modifier,
    bgColor: Color,
    iconColor: Color,
    onFlashModeSelected: (FlashMode) -> Unit,
    currentFlashMode: FlashMode,
    hasFlash: Boolean
) {
    var expanded by remember { mutableStateOf(false) } // State to track expansion
    val rotationAngle by animateFloatAsState(
        targetValue = if (expanded) 45f else 0f,
        label = "45degrees"
    ) // Animate rotation
    var currentFlashState by remember { mutableStateOf(currentFlashMode) }
    val currentIcon = when (currentFlashState) {
        FlashMode.BOTH -> ImageVector.vectorResource(R.drawable.ic_light_both)
        FlashMode.FLASH -> ImageVector.vectorResource(R.drawable.ic_light_flash)
        FlashMode.SCREEN -> ImageVector.vectorResource(R.drawable.ic_light_screen)
        FlashMode.NONE -> ImageVector.vectorResource(R.drawable.ic_light_none)
        else -> ImageVector.vectorResource(R.drawable.ic_light_both)
    }
    LaunchedEffect(currentFlashMode) {
        currentFlashState = currentFlashMode
    }

    if (currentFlashState != FlashMode.STROBE) {
        Box(
            modifier,
            contentAlignment = Alignment.BottomEnd
        ) {
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                // Show options only when expanded
                if (expanded) {
                    if (currentFlashState != FlashMode.BOTH && hasFlash)
                        FabOption(
                            bgColor,
                            iconColor,
                            label = "Both",
                            onClick = {
                                onFlashModeSelected(FlashMode.BOTH)
                                currentFlashState = FlashMode.BOTH
                                expanded = false
                            },
                            iconVector = ImageVector.vectorResource(R.drawable.ic_light_both)
                        )
                    if (currentFlashState != FlashMode.FLASH && hasFlash)
                        FabOption(
                            bgColor,
                            iconColor,
                            label = "Flash",
                            onClick = {
                                onFlashModeSelected(FlashMode.FLASH)
                                currentFlashState = FlashMode.FLASH
                                expanded = false
                            },
                            iconVector = ImageVector.vectorResource(R.drawable.ic_light_flash)
                        )
                    if (currentFlashState != FlashMode.SCREEN)
                        FabOption(
                            bgColor,
                            iconColor,
                            label = "Screen",
                            onClick = {
                                onFlashModeSelected(FlashMode.SCREEN)
                                currentFlashState = FlashMode.SCREEN
                                expanded = false
                            },
                            iconVector = ImageVector.vectorResource(R.drawable.ic_light_screen)
                        )
                    if (currentFlashState != FlashMode.NONE)
                        FabOption(
                            bgColor,
                            iconColor,
                            label = "None",
                            onClick = {
                                onFlashModeSelected(FlashMode.NONE)
                                currentFlashState = FlashMode.NONE
                                expanded = false
                            },
                            iconVector = ImageVector.vectorResource(R.drawable.ic_light_none)
                        )
                }
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