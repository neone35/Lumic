package com.arturmaslov.lumic.cache

import android.content.SharedPreferences
import androidx.compose.ui.graphics.Color
import com.arturmaslov.lumic.utils.Cache
import com.arturmaslov.lumic.utils.Constants.COLOR_INITIAL
import kotlinx.coroutines.CoroutineDispatcher

abstract class ColorSettingCache(dispatcher: CoroutineDispatcher) :
    Cache<Long>(dispatcher)

class ColorSettingCacheImpl(
    dispatcher: CoroutineDispatcher,
    private val sharedPreferences: SharedPreferences
) : ColorSettingCache(dispatcher) {

    override suspend fun getFromStorage(): Long {
        val floatSetting = sharedPreferences.getLong(COLOR_SETTINGS_KEY, COLOR_INITIAL)
        return floatSetting
    }

    override suspend fun saveInStorage(value: Long) {
        sharedPreferences.edit().putLong(COLOR_SETTINGS_KEY, value).apply()
    }

    override suspend fun clearStorage() {
        sharedPreferences.edit().putLong(COLOR_SETTINGS_KEY, 0L).apply()
    }

    companion object {
        const val COLOR_SETTINGS_KEY = "color_setting"
    }

}