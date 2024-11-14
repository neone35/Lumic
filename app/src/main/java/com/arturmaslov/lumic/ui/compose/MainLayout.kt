package com.arturmaslov.lumic.ui.compose

import android.content.Context
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.arturmaslov.lumic.MainActivity
import com.arturmaslov.lumic.ui.theme.LumicTheme
import com.arturmaslov.lumic.utils.PermissionStatus

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
    if (cameraAllowed && audioRecordAllowed) {
        Text(text = "Times Flashed: $timesFlashed")
        if (!hasFlash) {
            Text(text = "Camera has no flashlight")
        }
    } else {
        navToPermissionScreen()
    }
}
