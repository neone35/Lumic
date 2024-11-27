package com.arturmaslov.lumic.cache

import android.content.SharedPreferences
import androidx.compose.ui.graphics.Color
import com.arturmaslov.lumic.utils.Cache
import com.arturmaslov.lumic.utils.Constants.COLOR_INITIAL
import kotlinx.coroutines.CoroutineDispatcher

abstract class ColorSettingCache(dispatcher: CoroutineDispatcher) :
    Cache<Int>(dispatcher)

class ColorSettingCacheImpl(
    dispatcher: CoroutineDispatcher,
    private val sharedPreferences: SharedPreferences
) : ColorSettingCache(dispatcher) {

    override suspend fun getFromStorage(): Int {
        val intSetting = sharedPreferences
            .getInt(COLOR_SETTINGS_KEY, COLOR_INITIAL)
        return intSetting
    }

    override suspend fun saveInStorage(value: Int) {
        sharedPreferences.edit().putInt(COLOR_SETTINGS_KEY, value).apply()
    }

    override suspend fun clearStorage() {
        sharedPreferences.edit().putInt(COLOR_SETTINGS_KEY, 0).apply()
    }

    companion object {
        const val COLOR_SETTINGS_KEY = "color_setting"
    }

}