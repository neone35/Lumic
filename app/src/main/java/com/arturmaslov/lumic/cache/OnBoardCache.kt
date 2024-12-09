package com.arturmaslov.lumic.cache

import android.content.SharedPreferences
import com.arturmaslov.lumic.utils.Cache
import kotlinx.coroutines.CoroutineDispatcher

abstract class OnBoardCache(dispatcher: CoroutineDispatcher) :
    Cache<Boolean>(dispatcher)

class OnBoardCacheImpl(
    dispatcher: CoroutineDispatcher,
    private val sharedPreferences: SharedPreferences
) : OnBoardCache(dispatcher) {

    override suspend fun getFromStorage(): Boolean {
        val intSetting = sharedPreferences
            .getBoolean(ONBOARD_SETTING_KEY, false)
        return intSetting
    }

    override suspend fun saveInStorage(value: Boolean) {
        sharedPreferences.edit().putBoolean(ONBOARD_SETTING_KEY, value).apply()
    }

    override suspend fun clearStorage() {
        sharedPreferences.edit().putBoolean(ONBOARD_SETTING_KEY, false).apply()
    }

    companion object {
        const val ONBOARD_SETTING_KEY = "onboard_setting"
    }

}