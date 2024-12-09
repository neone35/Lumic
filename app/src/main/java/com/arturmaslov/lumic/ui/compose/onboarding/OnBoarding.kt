package com.arturmaslov.lumic.ui.compose.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.canopas.lib.showcase.IntroShowcase
import com.canopas.lib.showcase.component.ShowcaseStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnBoarding(
    onComplete: () -> Unit
) {
    var showAppIntro by remember {
        mutableStateOf(true)
    }
    val highlightSize = 56.dp
    val bgAlpha = 0.9f
    val bgColor = Color(0xFF1C0A00)
    val targetBlinkColor = Color.White
    val perimeterPadding = 16.dp

    IntroShowcase(
        showIntroShowCase = showAppIntro,
        dismissOnClickOutside = false,
        onShowCaseCompleted = onComplete,
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Transparent)
                .padding(perimeterPadding)
        ) {
            FloatingActionButton(
                onClick = {},
                modifier = Modifier
                    .padding(top = 200.dp)
                    .introShowCaseTarget(
                        index = 0,
                        style = ShowcaseStyle.Default.copy(
                            backgroundColor = bgColor, // specify color of background
                            backgroundAlpha = bgAlpha, // specify transparency of background
                            targetCircleColor = targetBlinkColor // specify color of target circle
                        ),
                        // specify the content to show to introduce app feature
                        content = {
                            Column {
                                Text(
                                    text = "Choose background color",
                                    color = Color.White,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Any color you like to flash on screen",
                                    color = Color.White,
                                    fontSize = 16.sp
                                )
                            }
                        }
                    )
                    .align(Alignment.Center)
                    .size(highlightSize),
                containerColor = Color.Black,
                contentColor = Color.White,
                elevation = FloatingActionButtonDefaults.elevation(6.dp)
            ) {}
            FloatingActionButton(
                onClick = {},
                modifier = Modifier
                    .padding(end = 200.dp)
                    .introShowCaseTarget(
                        index = 1,
                        style = ShowcaseStyle.Default.copy(
                            backgroundColor = bgColor, // specify color of background
                            backgroundAlpha = bgAlpha, // specify transparency of background
                            targetCircleColor = targetBlinkColor // specify color of target circle
                        ),
                        // specify the content to show to introduce app feature
                        content = {
                            Column {
                                Text(
                                    text = "Main settings",
                                    color = Color.White,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Adjust microphone sensitivity and flash duration",
                                    color = Color.White,
                                    fontSize = 16.sp
                                )
                            }
                        }
                    )
                    .align(Alignment.Center)
                    .size(highlightSize),
                containerColor = Color.Black,
                contentColor = Color.White,
                elevation = FloatingActionButtonDefaults.elevation(6.dp)
            ) {}
            FloatingActionButton(
                onClick = {},
                modifier = Modifier
                    .padding(start = 200.dp)
                    .introShowCaseTarget(
                        index = 2,
                        style = ShowcaseStyle.Default.copy(
                            backgroundColor = bgColor, // specify color of background
                            backgroundAlpha = bgAlpha, // specify transparency of background
                            targetCircleColor = targetBlinkColor // specify color of target circle
                        ),
                        // specify the content to show to introduce app feature
                        content = {
                            Column {
                                Text(
                                    text = "Settings sets",
                                    color = Color.White,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Save different settings for different surroundings",
                                    color = Color.White,
                                    fontSize = 16.sp
                                )
                            }
                        }
                    )
                    .align(Alignment.Center)
                    .size(highlightSize),
                containerColor = Color.Black,
                contentColor = Color.White,
                elevation = FloatingActionButtonDefaults.elevation(6.dp)
            ) {}
            FloatingActionButton(
                onClick = {},
                modifier = Modifier
                    .introShowCaseTarget(
                        index = 3,
                        style = ShowcaseStyle.Default.copy(
                            backgroundColor = bgColor, // specify color of background
                            backgroundAlpha = bgAlpha, // specify transparency of background
                            targetCircleColor = targetBlinkColor // specify color of target circle
                        ),
                        // specify the content to show to introduce app feature
                        content = {
                            Column {
                                Text(
                                    text = "Flash mode",
                                    color = Color.White,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Choose between screen, flash or both",
                                    color = Color.White,
                                    fontSize = 16.sp
                                )
                            }
                        }
                    )
                    .align(Alignment.BottomStart)
                    .size(highlightSize),
                containerColor = Color.Black,
                contentColor = Color.White,
                elevation = FloatingActionButtonDefaults.elevation(6.dp)
            ) {}
            FloatingActionButton(
                onClick = {},
                modifier = Modifier
                    .introShowCaseTarget(
                        index = 4,
                        style = ShowcaseStyle.Default.copy(
                            backgroundColor = bgColor, // specify color of background
                            backgroundAlpha = bgAlpha, // specify transparency of background
                            targetCircleColor = targetBlinkColor // specify color of target circle
                        ),
                        // specify the content to show to introduce app feature
                        content = {
                            Column {
                                Text(
                                    text = "Special strobe mode",
                                    color = Color.White,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Flashlight will blink as fast as it can",
                                    color = Color.White,
                                    fontSize = 16.sp
                                )
                            }
                        }
                    )
                    .align(Alignment.BottomEnd)
                    .size(highlightSize),
                containerColor = Color.Black,
                contentColor = Color.White,
                elevation = FloatingActionButtonDefaults.elevation(6.dp)
            ) {}
            FloatingActionButton(
                onClick = {},
                modifier = Modifier
                    .padding(top = 50.dp)
                    .introShowCaseTarget(
                        index = 5,
                        style = ShowcaseStyle.Default.copy(
                            backgroundColor = bgColor, // specify color of background
                            backgroundAlpha = bgAlpha, // specify transparency of background
                            targetCircleColor = targetBlinkColor // specify color of target circle
                        ),
                        // specify the content to show to introduce app feature
                        content = {
                            Column {
                                Text(
                                    text = "Locked mode",
                                    color = Color.White,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Switches to fullscreen, max brightness and disables touch input",
                                    color = Color.White,
                                    fontSize = 16.sp
                                )
                            }
                        }
                    )
                    .align(Alignment.TopCenter)
                    .size(highlightSize),
                containerColor = Color.Black,
                contentColor = Color.White,
                elevation = FloatingActionButtonDefaults.elevation(6.dp)
            ) {
                Icon(
                    Icons.Filled.Email,
                    contentDescription = "Email"
                )
            }
        }
    }
}