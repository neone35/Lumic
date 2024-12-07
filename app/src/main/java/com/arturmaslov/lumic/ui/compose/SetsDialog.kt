package com.arturmaslov.lumic.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.arturmaslov.lumic.R
import com.arturmaslov.lumic.data.UserSetting
import com.arturmaslov.lumic.ui.compose.main.ControlButton
import com.arturmaslov.lumic.ui.theme.LumicTheme
import com.arturmaslov.lumic.utils.ColorMode
import com.arturmaslov.lumic.utils.Constants.FLASH_ON_DURATION_INITIAL
import com.arturmaslov.lumic.utils.Constants.SENSITIVITY_THRESHOLD_INITIAL
import com.arturmaslov.lumic.utils.Constants.SETS_LIST_LENGTH
import com.arturmaslov.lumic.utils.FlashMode
import com.arturmaslov.lumic.utils.modifyColor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
@Preview(showBackground = true)
fun PreviewSetsDialog() {
    LumicTheme {
        SetsDialog(
            onSetSaved = {},
            onSetActivated = {},
            onSetsDismiss = {},
            allUserSettings = listOf(
                UserSetting(
                    id = 1,
                    userId = 1,
                    colorSetting = 0xFF444444.toInt(),
                    flashDuration = FLASH_ON_DURATION_INITIAL,
                    flashMode = FlashMode.BOTH,
                    sensitivity = SENSITIVITY_THRESHOLD_INITIAL
                )
            ),
            activeSetId = 0
        )
    }
}

@Composable
fun SetsDialog(
    onSetSaved: (setId: Int) -> Unit,
    onSetActivated: (setId: Int) -> Unit,
    onSetsDismiss: () -> Unit,
    allUserSettings: List<UserSetting>,
    activeSetId: Int
) {
    var checkedId by remember { mutableIntStateOf(activeSetId) }
    var currentUserSets by remember { mutableStateOf(allUserSettings) }

    // setup list of user settings
    val tempFixedList = emptyList<UserSetting>().toMutableList()
    tempFixedList.addAll(allUserSettings)
    tempFixedList.addAll(List(SETS_LIST_LENGTH - allUserSettings.size) {UserSetting()})
    currentUserSets = tempFixedList

    Dialog(
        onDismissRequest = { onSetsDismiss() }
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.small
        ) {
            LazyColumn(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Top),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                // dialog title
                item {
                    Text(
                        text = "Saved settings",
                        style = MaterialTheme.typography.titleSmall
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
                // column titles
                item {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "active",
                            style = MaterialTheme.typography.labelSmall
                        )
                        Text(
                            text = "duration",
                            style = MaterialTheme.typography.labelSmall
                        )
                        Text(
                            text = "mode",
                            style = MaterialTheme.typography.labelSmall
                        )
                        Text(
                            text = "sensitivity",
                            style = MaterialTheme.typography.labelSmall
                        )
                        Text(
                            text = "save",
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
                // set rows
                currentUserSets.forEachIndexed { index, userSetting ->
                    item {
                        val setId = index + 1
                        SetCard(
                            userSetting = userSetting,
                            onSetSaved = {
                                CoroutineScope(Dispatchers.Main).launch {
                                    onSetSaved(setId)
                                    delay(200)
                                    checkedId = setId
                                    onSetActivated(setId)
                                }
                            },
                            onSetActivated = {
                                checkedId = setId
                                onSetActivated(setId)
                            },
                            checkedId = checkedId
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SetCard(
    userSetting: UserSetting,
    onSetSaved: () -> Unit,
    onSetActivated: () -> Unit,
    checkedId: Int = -1
) {
    val checked = checkedId == userSetting.id

    val checkIcon = if (checked) {
        ImageVector.vectorResource(R.drawable.ic_checked)
    } else {
        ImageVector.vectorResource(R.drawable.ic_unchecked)
    }

    val setItemsColor = userSetting.colorSetting ?: Color.DarkGray.toArgb()
    val rowBackground = if (checked) Color(setItemsColor) else Color.Black
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(rowBackground)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        // activate check
        val colorModeOfChecked = if (checked) ColorMode.DARKER else ColorMode.LIGHTER
        ControlButton(
            bgTint = setItemsColor,
            onControlButtonClick = {
                onSetActivated()
            },
            iconVector = checkIcon,
            contentDescription = stringResource(R.string.check_uncheck),
            size = 50.dp,
            colorMode = colorModeOfChecked
        )

        val textColor = Color(setItemsColor.modifyColor(colorModeOfChecked.value))
        // color of the set
        // flash duration of the set
        Text(
            modifier = Modifier.width(50.dp),
            text = userSetting.flashDuration?.toInt().toString(),
            color = textColor,
            textAlign = TextAlign.Center
        )
        // flash mode icon of the set
        val flashModeIcon = when (userSetting.flashMode) {
            FlashMode.BOTH -> ImageVector.vectorResource(R.drawable.ic_light_both)
            FlashMode.SCREEN -> ImageVector.vectorResource(R.drawable.ic_light_screen)
            FlashMode.FLASH -> ImageVector.vectorResource(R.drawable.ic_light_flash)
            FlashMode.NONE -> ImageVector.vectorResource(R.drawable.ic_light_none)
            FlashMode.STROBE -> ImageVector.vectorResource(R.drawable.ic_strobe_on)
            else -> ImageVector.vectorResource(R.drawable.ic_light_none)
        }
        ControlButton(
            bgTint = setItemsColor,
            onControlButtonClick = {},
            iconVector = flashModeIcon,
            contentDescription = stringResource(R.string.flash_mode_icon),
            size = 50.dp,
            rippleEnabled = false
        )
        // sensitivity of the set
        Text(
            modifier = Modifier.width(50.dp),
            text = userSetting.sensitivity?.toInt().toString(),
            color = textColor,
            textAlign = TextAlign.Center
        )

        // save set icon
        ControlButton(
            bgTint = setItemsColor,
            onControlButtonClick = {
                onSetSaved()
            },
            iconVector = ImageVector.vectorResource(R.drawable.ic_album),
            contentDescription = stringResource(R.string.save_set),
            size = 50.dp,
            colorMode = colorModeOfChecked
        )
    }
}