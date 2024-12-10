package com.arturmaslov.lumic.di

import android.content.Context
import com.arturmaslov.lumic.cache.FlashDurationSettingCache
import com.arturmaslov.lumic.cache.FlashSettingCache
import com.arturmaslov.lumic.cache.SensitivitySettingCache
import com.arturmaslov.lumic.utils.AudioUtils
import com.arturmaslov.lumic.utils.CameraUtils
import com.arturmaslov.lumic.utils.FirebaseUtils
import com.google.firebase.analytics.FirebaseAnalytics
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule = module {
    single { provideCameraUtils(androidContext(), get(), get(), get()) }
    single { provideAudioUtils(androidContext()) }
    single { provideFirebaseUtils() }
}

private fun provideCameraUtils(
    context: Context,
    sensitivitySettingCache: SensitivitySettingCache,
    flashSettingsCache: FlashSettingCache,
    flashDurationSettingCache: FlashDurationSettingCache
) = CameraUtils(context, sensitivitySettingCache, flashSettingsCache, flashDurationSettingCache)

private fun provideAudioUtils(context: Context) = AudioUtils(context)

private fun provideFirebaseUtils() = FirebaseUtils