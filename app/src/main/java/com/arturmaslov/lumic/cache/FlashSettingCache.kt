package com.arturmaslov.lumic.cache

import android.content.SharedPreferences
import com.arturmaslov.lumic.utils.Cache
import com.arturmaslov.lumic.utils.FlashMode
import kotlinx.coroutines.CoroutineDispatcher

abstract class FlashSettingCache(dispatcher: CoroutineDispatcher) :
    Cache<FlashMode>(dispatcher)

class FlashSettingCacheImpl(
    dispatcher: CoroutineDispatcher,
    private val sharedPreferences: SharedPreferences
) : FlashSettingCache(dispatcher) {

    override suspend fun getFromStorage(): FlashMode {
        val stringSetting = sharedPreferences
            .getString(FLASH_SETTING_KEY, FLASH_MODE_INITIAL)
            ?: FLASH_MODE_INITIAL
        val flashModeSetting = getFlashMode(stringSetting)
        return flashModeSetting
    }

    override suspend fun saveInStorage(value: FlashMode) {
        val stringSetting = getFlashModeString(value)
        sharedPreferences.edit().putString(FLASH_SETTING_KEY, stringSetting).apply()
    }

    override suspend fun clearStorage() {
        sharedPreferences.edit().putString(FLASH_SETTING_KEY, FLASH_MODE_INITIAL).apply()
    }

    private fun getFlashModeString(flashMode: FlashMode): String {
        return when (flashMode) {
            FlashMode.NONE -> FlashMode.NONE.string
            FlashMode.SCREEN -> FlashMode.SCREEN.string
            FlashMode.BOTH -> FlashMode.BOTH.string
            FlashMode.FLASH -> FlashMode.FLASH.string
            FlashMode.STROBE -> FlashMode.STROBE.string
            else -> FLASH_MODE_INITIAL
        }
    }

    private fun getFlashMode(flashModeString: String): FlashMode {
        return when (flashModeString) {
            FlashMode.NONE.string -> FlashMode.NONE
            FlashMode.SCREEN.string -> FlashMode.SCREEN
            FlashMode.BOTH.string -> FlashMode.BOTH
            FlashMode.FLASH.string -> FlashMode.FLASH
            FlashMode.STROBE.string -> FlashMode.STROBE
            else -> FlashMode.NONE
        }
    }

    companion object {
        const val FLASH_SETTING_KEY = "flash_setting"
        val FLASH_MODE_INITIAL: String = FlashMode.BOTH.string
    }

}