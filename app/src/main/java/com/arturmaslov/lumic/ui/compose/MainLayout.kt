package com.arturmaslov.lumic.ui.compose

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arturmaslov.lumic.MainActivity
import com.arturmaslov.lumic.ui.theme.LumicTheme
import com.arturmaslov.lumic.utils.Constants.FLASH_ON_DURATION_MS
import com.arturmaslov.lumic.utils.PermissionStatus
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive

@Composable
@Preview(showBackground = true)
fun PreviewMainScreen() {
    LumicTheme {
        MainScreen(
            cameraAllowed = true,
            audioRecordAllowed = true,
            hasFlash = true,
            timesFlashed = 0,
            navToPermissionScreen = {}
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
    navToPermissionScreen: () -> Unit,
) {
    val bgColor = remember { mutableStateOf(Color.Black) }
    LaunchedEffect(timesFlashed) { // Restart the effect when the timesFlashed changes
        if (timesFlashed > 0) {
            bgColor.value = Color.Red
            delay(FLASH_ON_DURATION_MS)
            bgColor.value = Color.Black
        }
    }

    if (cameraAllowed && audioRecordAllowed) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(bgColor.value),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Times Flashed: $timesFlashed")
            if (!hasFlash) {
                Text(text = "Camera has no flashlight")
            }
        }
    } else {
        navToPermissionScreen()
    }
}
