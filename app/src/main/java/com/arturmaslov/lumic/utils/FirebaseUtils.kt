package com.arturmaslov.lumic.utils

import androidx.core.os.bundleOf
import com.arturmaslov.lumic.data.UserSetting
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import timber.log.Timber

object FirebaseUtils {

    private var firebaseAnalytics: FirebaseAnalytics? = Firebase.analytics

    fun recordUserSetEvent(savedSettingSet: UserSetting, setEvent: SetEvent) {
        Timber.i("FirebaseUtils recordSetEvent")
        val bundle = bundleOf(
            "setId" to savedSettingSet.id,
            "userId" to savedSettingSet.userId,
            "color" to savedSettingSet.colorSetting,
            "flashDuration" to savedSettingSet.flashDuration,
            "flashMode" to savedSettingSet.flashMode,
            "sensitivity" to savedSettingSet.sensitivity,
        )
        when (setEvent) {
            SetEvent.SET_SAVED -> firebaseAnalytics?.logEvent(
                "record_saved_set",
                bundle
            )
            SetEvent.SET_ACTIVATED -> firebaseAnalytics?.logEvent(
                "record_activated_set",
                bundle
            )
        }
    }

    fun recordFlashModeChange(flashMode: FlashMode) {
        Timber.i("FirebaseUtils recordFlashModeChange")

        val bundle = bundleOf(
            "flashMode" to flashMode.string,
        )
        firebaseAnalytics?.logEvent(
            "flash_mode_change",
            bundle
        )
    }

    fun recordLock(hasBeenLocked: Boolean) {
        Timber.i("FirebaseUtils recordLock")

        val bundle = bundleOf(
            "locked" to hasBeenLocked,
        )
        if (hasBeenLocked) {
            firebaseAnalytics?.logEvent(
                "record_lock",
                bundle
            )
        } else {
            firebaseAnalytics?.logEvent(
                "record_unlock",
                bundle
            )
        }
    }

    fun recordFlashDurationChange(flashDuration: Float) {
        Timber.i("FirebaseUtils recordFlashDurationChange")

        val bundle = bundleOf(
            "flashDuration" to flashDuration,
        )
        firebaseAnalytics?.logEvent(
            "flash_duration_change",
            bundle
        )
    }

    fun recordColorChange(color: Int) {
        Timber.i("FirebaseUtils recordColorChange")

        val bundle = bundleOf(
            "color" to color,
        )
        firebaseAnalytics?.logEvent(
            "color_change",
            bundle
        )
    }

    fun recordSensitivityChange(sensitivity: Float) {
        Timber.i("FirebaseUtils recordSensitivityChange")

        val bundle = bundleOf(
            "sensitivity" to sensitivity,
        )
        firebaseAnalytics?.logEvent(
            "sensitivity_change",
            bundle
        )
    }

}