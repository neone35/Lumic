package com.arturmaslov.lumic

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.unit.dp
import com.arturmaslov.lumic.ui.compose.main.ControlButton
import com.arturmaslov.lumic.ui.compose.main.MainControl
import com.arturmaslov.lumic.ui.compose.main.MultiChoiceFab
import com.arturmaslov.lumic.ui.theme.LumicTheme
import com.arturmaslov.lumic.utils.FlashMode
import org.junit.Rule
import org.junit.Test

class MainLayoutTest {

    @get:Rule val composeTestRule = createComposeRule()
    // use createAndroidComposeRule<YourActivity>() if you need access to an activity

    @Test
    fun mainControlViewsTest() {
        // Start the app
        composeTestRule.setContent {
            LumicTheme() {
                MainControl(
                    modifier = Modifier,
                    bgTint = Color.Magenta.toArgb(),
                    currentFlashMode = FlashMode.BOTH,
                    onFlashModeSelected = {}
                )
            }
        }
        val context = App.getAppContext()
        val colorPickerButton = composeTestRule.onNodeWithTag(context.getString(R.string.color_picker))
        val settingsButton = composeTestRule.onNodeWithTag(context.getString(R.string.settings))
        val playStopButton = composeTestRule.onNodeWithContentDescription(context.getString(R.string.play_stop))

        colorPickerButton.assertIsDisplayed()
        settingsButton.assertIsDisplayed()
        playStopButton.assertIsDisplayed()
        Thread.sleep(500)
        colorPickerButton.performClick()
        colorPickerButton.assertIsDisplayed()
        Thread.sleep(500)
        settingsButton.performClick()
        settingsButton.assertIsDisplayed()
        Thread.sleep(500)
        playStopButton.performClick()
        playStopButton.assertIsDisplayed()
    }

    @Test
    fun multiChoiceFabTest() {
        composeTestRule.setContent {
            LumicTheme() {
                MultiChoiceFab(
                    bgColor = Color.Magenta,
                    iconColor = Color(Color.Magenta.toArgb()),
                    onFlashModeSelected = { },
                    currentFlashMode = FlashMode.BOTH,
                    hasFlash = true
                )
            }
        }
        val context = App.getAppContext()
        val mainBigFAB = composeTestRule.onNodeWithTag("MCF")
        val onlyFlashSmallFAB = composeTestRule.onNodeWithText(context.getString(R.string.flash))
        val onlyScreenSmallFAB = composeTestRule.onNodeWithText(context.getString(R.string.screen))

        mainBigFAB.performClick()
        Thread.sleep(500)
        onlyFlashSmallFAB.assertIsDisplayed()
        onlyScreenSmallFAB.assertIsDisplayed()
        Thread.sleep(500)
        mainBigFAB.performClick()
        Thread.sleep(500)
        onlyFlashSmallFAB.assertIsNotDisplayed()
        onlyScreenSmallFAB.assertIsNotDisplayed()
    }

    @Test
    fun strobeModeButtonTest() {
        composeTestRule.setContent {
            LumicTheme() {
                val strobeInactiveIcon = ImageVector.vectorResource(R.drawable.ic_strobe_off)
                ControlButton(
                    bgTint = Color.Magenta.toArgb(),
                    onControlButtonClick = { },
                    iconVector = strobeInactiveIcon,
                    contentDescription = stringResource(R.string.strobe_mode),
                    size = 50.dp
                )
            }
        }
        val context = App.getAppContext()

        val strobeButton = composeTestRule.onNodeWithContentDescription(context.getString(R.string.strobe_mode))
        strobeButton.assertIsDisplayed()
        strobeButton.performClick()
        Thread.sleep(500)
        strobeButton.assertIsDisplayed()
        Thread.sleep(500)
        strobeButton.assertIsDisplayed()
    }
}