package com.arturmaslov.lumic.di

import android.content.Context
import com.arturmaslov.lumic.cache.ActiveSettingsCacheImpl
import com.arturmaslov.lumic.cache.ActiveUserSettingsCache
import com.arturmaslov.lumic.cache.ColorSettingCache
import com.arturmaslov.lumic.cache.ColorSettingCacheImpl
import com.arturmaslov.lumic.cache.FlashDurationSettingCache
import com.arturmaslov.lumic.cache.FlashDurationSettingCacheImpl
import com.arturmaslov.lumic.cache.FlashSettingCache
import com.arturmaslov.lumic.cache.FlashSettingCacheImpl
import com.arturmaslov.lumic.cache.OnBoardCache
import com.arturmaslov.lumic.cache.OnBoardCacheImpl
import com.arturmaslov.lumic.cache.SensitivitySettingCache
import com.arturmaslov.lumic.cache.SensitivitySettingCacheImpl
import com.arturmaslov.lumic.utils.Constants
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext

import org.koin.core.qualifier.named
import org.koin.dsl.module

val cacheModule = module {
    single(named(Constants.PREFS_DEFAULT)) {
        androidContext().getSharedPreferences(
            Constants.PREFS_DEFAULT,
            Context.MODE_PRIVATE
        )
    }

    single<SensitivitySettingCache> {
        SensitivitySettingCacheImpl(
            dispatcher = Dispatchers.IO,
            sharedPreferences = get(named(Constants.PREFS_DEFAULT))
        )
    }

    single<ColorSettingCache> {
        ColorSettingCacheImpl(
            dispatcher = Dispatchers.IO,
            sharedPreferences = get(named(Constants.PREFS_DEFAULT))
        )
    }

    single<FlashSettingCache> {
        FlashSettingCacheImpl(
            dispatcher = Dispatchers.IO,
            sharedPreferences = get(named(Constants.PREFS_DEFAULT))
        )
    }

    single<FlashDurationSettingCache> {
        FlashDurationSettingCacheImpl(
            dispatcher = Dispatchers.IO,
            sharedPreferences = get(named(Constants.PREFS_DEFAULT))
        )
    }

    single<ActiveUserSettingsCache> {
        ActiveSettingsCacheImpl(
            dispatcher = Dispatchers.IO,
            sharedPreferences = get(named(Constants.PREFS_DEFAULT))
        )
    }

    single<OnBoardCache> {
        OnBoardCacheImpl(
            dispatcher = Dispatchers.IO,
            sharedPreferences = get(named(Constants.PREFS_DEFAULT))
        )
    }
}