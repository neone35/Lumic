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

@Composable
@Preview(showBackground = true)
fun PreviewMainScreen() {
    LumicTheme {
        MainScreen(
            cameraPermissionStatus = MainActivity.PermissionStatus.GRANTED,
            hasFlash = true,
            timesFlashed = 0
        )
    }
}


@Composable
fun MainScreen(
    modifier: Modifier = Modifier.fillMaxSize(),
    cameraPermissionStatus: MainActivity.PermissionStatus,
    hasFlash: Boolean,
    timesFlashed: Int
) {
    if (cameraPermissionStatus == MainActivity.PermissionStatus.GRANTED
        && hasFlash) {
        Text(text = "Times Flashed: $timesFlashed")
    } else {
        if (!hasFlash) {
            Text(text = "Camera has no flashlight")
        } else {
            Text(text = "Camera permission denied")
        }

    }
}
