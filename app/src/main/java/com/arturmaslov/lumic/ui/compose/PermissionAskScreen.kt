package com.arturmaslov.lumic.ui.compose

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.arturmaslov.lumic.MainActivity
import com.arturmaslov.lumic.R
import com.arturmaslov.lumic.ui.theme.LumicTheme
import com.arturmaslov.lumic.utils.getAppName

@Composable
@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showSystemUi = true
)
fun PreviewPermissionAskScreen() {
    LumicTheme {
        PermissionAskScreen(
            cameraAllowed = false,
            audioRecordAllowed = false,
            onRequestPermissions = { },
            navToMainScreen = { }
        )
    }
}

@Composable
fun PermissionAskScreen(
    cameraAllowed: Boolean,
    audioRecordAllowed: Boolean,
    onRequestPermissions: () -> Unit,
    navToMainScreen: () -> Unit
) {
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
//            if (!cameraAllowed && !audioRecordAllowed) {
//                Text(text = "Camera and audio permission denied", color = Color.Red, fontWeight = FontWeight.Bold)
//            } else if (!cameraAllowed) {
//                Text(text = "Camera permission denied", color = Color.Red, fontWeight = FontWeight.Bold)
//            } else if (!audioRecordAllowed) {
//                Text(text = "Audio permission denied", color = Color.Red, fontWeight = FontWeight.Bold)
//            }


            Spacer(modifier = Modifier.weight(1f))
            // Icon and explanation
            Icon(
                painter = painterResource(id = android.R.drawable.ic_dialog_info),
                contentDescription = stringResource(R.string.info_icon),
                tint = Color.White,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Explanation text
            Text(
                text = stringResource(R.string.permissions_required),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.permissions_explanation),
                fontSize = 16.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Button to request permissions
            Button(onClick = onRequestPermissions) {
                Text("Allow")
            }

            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = stringResource(R.string.permissions_sensitivity_notice),
                textAlign = TextAlign.Center,
                color = Color.LightGray,
                fontSize = 12.sp
            )
        }
    } else {
        // Permissions granted state
        navToMainScreen()
    }
}

