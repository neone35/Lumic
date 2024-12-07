package com.arturmaslov.lumic.cache

import android.content.SharedPreferences
import com.arturmaslov.lumic.utils.Cache
import com.arturmaslov.lumic.utils.Constants
import com.arturmaslov.lumic.utils.Constants.COLOR_INITIAL
import kotlinx.coroutines.CoroutineDispatcher

abstract class ActiveUserSettingsCache(dispatcher: CoroutineDispatcher) :
    Cache<Pair<Int, Int>>(dispatcher)

class ActiveSettingsCacheImpl(
    dispatcher: CoroutineDispatcher,
    private val sharedPreferences: SharedPreferences
) : ActiveUserSettingsCache(dispatcher) {

    override suspend fun getFromStorage(): Pair<Int, Int> {
        val userId = sharedPreferences.getInt(ACTIVE_USER_ID, 1)
        val settingId = sharedPreferences.getInt(ACTIVE_SETTING_ID_KEY, 1)
        return Pair(userId, settingId)
    }

    override suspend fun saveInStorage(value: Pair<Int, Int>) {
        sharedPreferences.edit().putInt(ACTIVE_USER_ID, value.first).apply()
        sharedPreferences.edit().putInt(ACTIVE_SETTING_ID_KEY, value.second).apply()
    }

    override suspend fun clearStorage() {
        sharedPreferences.edit().putInt(ACTIVE_USER_ID, 0).apply()
        sharedPreferences.edit().putInt(ACTIVE_SETTING_ID_KEY, 0).apply()
    }

    companion object {
        const val ACTIVE_SETTING_ID_KEY = "active_setting_id"
        const val ACTIVE_USER_ID = "active_user_id"
    }

}