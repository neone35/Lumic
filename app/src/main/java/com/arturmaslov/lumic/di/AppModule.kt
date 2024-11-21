package com.arturmaslov.lumic.di

import android.content.Context
import com.arturmaslov.lumic.cache.SensitivitySettingCache
import com.arturmaslov.lumic.utils.AudioUtils
import com.arturmaslov.lumic.utils.CameraUtils
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule = module {
    single { provideCameraUtils(androidContext(), get()) }
    single { provideAudioUtils(androidContext()) }
}

private fun provideCameraUtils(
    context: Context,
    sensitivitySettingCache: SensitivitySettingCache
) = CameraUtils(context, sensitivitySettingCache)

private fun provideAudioUtils(context: Context) = AudioUtils(context)