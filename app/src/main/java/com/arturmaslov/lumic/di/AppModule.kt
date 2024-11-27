package com.arturmaslov.lumic.di

import android.content.Context
import com.arturmaslov.lumic.cache.FlashSettingCache
import com.arturmaslov.lumic.cache.SensitivitySettingCache
import com.arturmaslov.lumic.utils.AudioUtils
import com.arturmaslov.lumic.utils.CameraUtils
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule = module {
    single { provideCameraUtils(androidContext(), get(), get()) }
    single { provideAudioUtils(androidContext()) }
}

private fun provideCameraUtils(
    context: Context,
    sensitivitySettingCache: SensitivitySettingCache,
    flashSettingsCache: FlashSettingCache
) = CameraUtils(context, sensitivitySettingCache, flashSettingsCache)

private fun provideAudioUtils(context: Context) = AudioUtils(context)