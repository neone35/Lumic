package com.arturmaslov.lumic.cache

import android.content.SharedPreferences
import androidx.compose.ui.graphics.Color
import com.arturmaslov.lumic.ui.compose.main.FlashModeState
import com.arturmaslov.lumic.utils.Cache
import com.arturmaslov.lumic.utils.Constants.COLOR_INITIAL
import com.arturmaslov.lumic.utils.Constants.FLASH_MODE_INITIAL
import kotlinx.coroutines.CoroutineDispatcher

abstract class FlashSettingCache(dispatcher: CoroutineDispatcher) :
    Cache<FlashModeState>(dispatcher)

class FlashSettingCacheImpl(
    dispatcher: CoroutineDispatcher,
    private val sharedPreferences: SharedPreferences
) : FlashSettingCache(dispatcher) {

    override suspend fun getFromStorage(): FlashModeState {
        val stringSetting = sharedPreferences
            .getString(FLASH_SETTING_KEY, FLASH_MODE_INITIAL)
            ?: FLASH_MODE_INITIAL
        val returnSetting = getFlashModeState(stringSetting)
        return returnSetting
    }

    override suspend fun saveInStorage(value: FlashModeState) {
        val stringSetting = getFlashModeString(value)
        sharedPreferences.edit().putString(FLASH_SETTING_KEY, stringSetting).apply()
    }

    override suspend fun clearStorage() {
        sharedPreferences.edit().putString(FLASH_SETTING_KEY, FLASH_MODE_INITIAL).apply()
    }

    private fun getFlashModeString(flashModeState: FlashModeState): String {
        return when (flashModeState) {
            FlashModeState.NONE -> FlashModeState.NONE.stateString
            FlashModeState.SCREEN -> FlashModeState.SCREEN.stateString
            FlashModeState.BOTH -> FlashModeState.BOTH.stateString
            FlashModeState.FLASH -> FlashModeState.FLASH.stateString
            else -> FLASH_MODE_INITIAL
        }
    }

    private fun getFlashModeState(flashModeString: String): FlashModeState {
        return when (flashModeString) {
            FlashModeState.NONE.stateString -> FlashModeState.NONE
            FlashModeState.SCREEN.stateString -> FlashModeState.SCREEN
            FlashModeState.BOTH.stateString -> FlashModeState.BOTH
            FlashModeState.FLASH.stateString -> FlashModeState.FLASH
            else -> FlashModeState.NONE
        }
    }

    companion object {
        const val FLASH_SETTING_KEY = "flash_setting"
    }

}