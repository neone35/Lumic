package com.arturmaslov.lumic.ui.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.arturmaslov.lumic.ui.theme.LumicTheme
import com.github.skydoves.colorpicker.compose.ColorEnvelope
import com.github.skydoves.colorpicker.compose.ColorPickerController
import com.github.skydoves.colorpicker.compose.HsvColorPicker

@Composable
@Preview(showBackground = true)
fun PreviewColorPickerDialog() {
    LumicTheme {
        ColorPickerDialog(
            bgColor = remember { mutableIntStateOf(Color.DarkGray.toArgb()) },
            onColorPickerDismiss = {}
        )
    }
}

@Composable
fun ColorPickerDialog(
    bgColor: MutableState<Int>,
    onColorPickerDismiss: () -> Unit = {},
    onColorSelected: (Int) -> Unit = {}
) {
    Dialog(
        onDismissRequest = {
            onColorPickerDismiss()
        }
    ) {
        Column(
            modifier = Modifier
                .height(300.dp)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.Top),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Choose colour",
                style = MaterialTheme.typography.titleSmall
            )
            val controller = remember { ColorPickerController() }
            HsvColorPicker(
                modifier = Modifier.padding(16.dp),
                controller = controller,
                initialColor = Color(bgColor.value),
                onColorChanged = { colorEnvelope: ColorEnvelope ->
                    bgColor.value = colorEnvelope.color.toArgb()
                    onColorSelected(colorEnvelope.color.toArgb())
                }
            )

        }
    }
}