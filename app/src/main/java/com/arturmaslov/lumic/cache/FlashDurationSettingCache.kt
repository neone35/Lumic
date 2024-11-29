package com.arturmaslov.lumic.cache

import android.content.SharedPreferences
import com.arturmaslov.lumic.utils.Cache
import com.arturmaslov.lumic.utils.Constants.FLASH_ON_DURATION_INITIAL
import kotlinx.coroutines.CoroutineDispatcher

abstract class FlashDurationSettingCache(dispatcher: CoroutineDispatcher) :
    Cache<Float>(dispatcher)

class FlashDurationSettingCacheImpl(
    dispatcher: CoroutineDispatcher,
    private val sharedPreferences: SharedPreferences
) : FlashDurationSettingCache(dispatcher) {

    override suspend fun getFromStorage(): Float {
        val floatSetting = sharedPreferences
            .getFloat(FLASH_DURATION_SETTINGS_KEY, FLASH_ON_DURATION_INITIAL)
        return floatSetting
    }

    override suspend fun saveInStorage(value: Float) {
        sharedPreferences.edit().putFloat(FLASH_DURATION_SETTINGS_KEY, value).apply()
    }

    override suspend fun clearStorage() {
        sharedPreferences.edit().putFloat(FLASH_DURATION_SETTINGS_KEY, 0f).apply()
    }

    companion object {
        const val FLASH_DURATION_SETTINGS_KEY = "flash_duration"
    }

}