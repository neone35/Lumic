package com.arturmaslov.lumic

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import com.arturmaslov.lumic.ui.compose.main.MainControl
import com.arturmaslov.lumic.ui.theme.LumicTheme
import com.arturmaslov.lumic.utils.FlashMode
import org.junit.Rule
import org.junit.Test

class MainControlTest {

    @get:Rule val composeTestRule = createComposeRule()
    // use createAndroidComposeRule<YourActivity>() if you need access to an activity

    @Test
    fun mainControlViewsTest() {
        // Start the app
        composeTestRule.setContent {
            LumicTheme() {
                MainControl(
                    modifier = Modifier,
                    bgTint = Color.Green.toArgb(),
                    currentFlashMode = FlashMode.BOTH,
                    onFlashModeSelected = {}
                )
            }
        }
        val context = App.getAppContext()

        composeTestRule.onNodeWithTag("color picker").assertIsDisplayed()
        composeTestRule.onNodeWithTag("settings dialog").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription(context.getString(R.string.play_stop)).assertIsDisplayed()
    }
}