package com.arturmaslov.lumic.ui.compose

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.arturmaslov.lumic.MainActivity
import com.arturmaslov.lumic.ui.theme.LumicTheme
import com.arturmaslov.lumic.utils.PermissionStatus

@Composable
@Preview(showBackground = true)
fun PreviewPermissionAskScreen() {
    LumicTheme {
        PermissionAskScreen(
            cameraPermissionStatus = PermissionStatus.DENIED,
            audioRecordPermissionStatus = PermissionStatus.DENIED,
            onRequestPermissions = { },
            navToMainScreen = { }
        )
    }
}

@Composable
fun PermissionAskScreen(
    cameraPermissionStatus: PermissionStatus,
    audioRecordPermissionStatus: PermissionStatus,
    onRequestPermissions: () -> Unit,
    navToMainScreen: () -> Unit
) {
    val cameraAllowed = cameraPermissionStatus == PermissionStatus.GRANTED
    val audioRecordAllowed = audioRecordPermissionStatus == PermissionStatus.GRANTED

    // Content displayed when permissions are denied
    if (!cameraAllowed || !audioRecordAllowed) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Display permission status
            if (!cameraAllowed && !audioRecordAllowed) {
                Text(text = "Camera and audio permission denied", color = Color.Red, fontWeight = FontWeight.Bold)
            } else if (!cameraAllowed) {
                Text(text = "Camera permission denied", color = Color.Red, fontWeight = FontWeight.Bold)
            } else if (!audioRecordAllowed) {
                Text(text = "Audio permission denied", color = Color.Red, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(24.dp))
            // Icon and explanation
            Icon(
                painter = painterResource(id = android.R.drawable.ic_dialog_info),
                contentDescription = "Info Icon",
                tint = Color.White,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Explanation text
            Text(
                text = "Permissions Required",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "To enable sound visualization, we need access to your camera and microphone. " +
                        "Without these permissions, the app cannot proceed.",
                fontSize = 16.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Button to request permissions
            Button(onClick = onRequestPermissions) {
                Text("Allow")
            }
        }
    } else {
        // Permissions granted state
        navToMainScreen()
    }
}

