package com.arturmaslov.lumic.cache

import android.content.SharedPreferences
import com.arturmaslov.lumic.utils.Cache
import com.arturmaslov.lumic.utils.Constants.SENSITIVITY_THRESHOLD_INITIAL
import kotlinx.coroutines.CoroutineDispatcher

abstract class SensitivitySettingCache(dispatcher: CoroutineDispatcher) :
    Cache<Float>(dispatcher)

class SensitivitySettingCacheImpl(
    dispatcher: CoroutineDispatcher,
    private val sharedPreferences: SharedPreferences
) : SensitivitySettingCache(dispatcher) {

    override suspend fun getFromStorage(): Float {
        val floatSetting = sharedPreferences.getFloat(SENSITIVITY_THRESHOLD_SETTINGS_KEY, SENSITIVITY_THRESHOLD_INITIAL)
        return floatSetting
    }

    override suspend fun saveInStorage(value: Float) {
        sharedPreferences.edit().putFloat(SENSITIVITY_THRESHOLD_SETTINGS_KEY, value).apply()
    }

    override suspend fun clearStorage() {
        sharedPreferences.edit().putFloat(SENSITIVITY_THRESHOLD_SETTINGS_KEY, 0f).apply()
    }

    companion object {
        const val SENSITIVITY_THRESHOLD_SETTINGS_KEY = "sensitivity_threshold"
    }

}