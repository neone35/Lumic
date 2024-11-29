package com.arturmaslov.lumic.ui.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.arturmaslov.lumic.ui.theme.LumicTheme
import com.arturmaslov.lumic.utils.Constants.SENSITIVITY_THRESHOLD_INITIAL
import com.arturmaslov.lumic.utils.Constants.SENSITIVITY_THRESHOLD_MAX
import com.arturmaslov.lumic.utils.Constants.SENSITIVITY_THRESHOLD_MIN
import com.arturmaslov.lumic.utils.Constants.SENSITIVITY_THRESHOLD_STEPS

@Composable
@Preview(showBackground = true)
fun PreviewSettingsDialog() {
    LumicTheme {
        SettingsDialog(
            onSettingsDismiss = {},
            onMicrophoneSliderValueSelected = {},
            currentSensitivityThreshold = SENSITIVITY_THRESHOLD_INITIAL
        )
    }
}

@Composable
fun SettingsDialog(
    onSettingsDismiss: () -> Unit = {},
    onMicrophoneSliderValueSelected: (Float) -> Unit,
    currentSensitivityThreshold: Float
) {

    Dialog(
        onDismissRequest = { onSettingsDismiss() }
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.small
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.Top),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "Settings",
                    style = MaterialTheme.typography.titleMedium
                )
                SliderItem(
                    sliderTitle = "Microphone sensitivity",
                    onSliderValueSelected = { threshold ->
                        onMicrophoneSliderValueSelected(threshold) },
                    currentValue = currentSensitivityThreshold,
                    minValue = SENSITIVITY_THRESHOLD_MIN,
                    maxValue = SENSITIVITY_THRESHOLD_MAX,
                    steps = SENSITIVITY_THRESHOLD_STEPS
                )
                Button(
                    onClick = { onSettingsDismiss() },
                ) {
                    Text(text = "Close")
                }
            }
        }
    }
}

@Composable
fun SliderItem(
    sliderTitle: String,
    onSliderValueSelected: (Float) -> Unit,
    currentValue: Float,
    minValue: Float,
    maxValue: Float,
    steps: Int
) {
    val slidingRange by remember {
        mutableStateOf(minValue..maxValue)
    }
    var sensitivityThreshold by remember { mutableFloatStateOf(currentValue) }
    Text(
        text = sliderTitle,
        style = MaterialTheme.typography.labelSmall
    )
    Slider(
        value = sensitivityThreshold,
        steps = steps,
        onValueChange = { value ->
            sensitivityThreshold = value
        },
        valueRange = slidingRange,
        onValueChangeFinished = {
            onSliderValueSelected(sensitivityThreshold)
        },
    )
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = slidingRange.start.toInt().toString(),
            style = MaterialTheme.typography.labelSmall
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = sensitivityThreshold.toString(),
            style = MaterialTheme.typography.labelSmall,
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = slidingRange.endInclusive.toInt().toString(),
            style = MaterialTheme.typography.labelSmall,
        )
    }
}