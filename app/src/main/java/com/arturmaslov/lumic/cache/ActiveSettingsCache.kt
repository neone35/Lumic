package com.arturmaslov.lumic.cache

import android.content.SharedPreferences
import com.arturmaslov.lumic.utils.Cache
import com.arturmaslov.lumic.utils.Constants
import com.arturmaslov.lumic.utils.Constants.COLOR_INITIAL
import kotlinx.coroutines.CoroutineDispatcher

abstract class ActiveUserSettingsCache(dispatcher: CoroutineDispatcher) :
    Cache<Pair<String, Int>>(dispatcher)

class ActiveSettingsCacheImpl(
    dispatcher: CoroutineDispatcher,
    private val sharedPreferences: SharedPreferences
) : ActiveUserSettingsCache(dispatcher) {

    override suspend fun getFromStorage(): Pair<String, Int> {
        val userName = sharedPreferences.getString(ACTIVE_USER_KEY, Constants.EMPTY_STRING) ?: Constants.EMPTY_STRING
        val settingId = sharedPreferences.getInt(ACTIVE_SETTING_ID_KEY, COLOR_INITIAL)
        return Pair(userName, settingId)
    }

    override suspend fun saveInStorage(value: Pair<String, Int>) {
        sharedPreferences.edit().putString(ACTIVE_USER_KEY, value.first).apply()
        sharedPreferences.edit().putInt(ACTIVE_SETTING_ID_KEY, value.second).apply()
    }

    override suspend fun clearStorage() {
        sharedPreferences.edit().putString(ACTIVE_USER_KEY, Constants.EMPTY_STRING).apply()
        sharedPreferences.edit().putInt(ACTIVE_SETTING_ID_KEY, 0).apply()
    }

    companion object {
        const val ACTIVE_SETTING_ID_KEY = "active_setting"
        const val ACTIVE_USER_KEY = "active_user"
    }

}