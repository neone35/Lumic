package com.arturmaslov.lumic

import android.Manifest
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import com.arturmaslov.lumic.cache.ColorSettingCache
import com.arturmaslov.lumic.cache.FlashSettingCache
import com.arturmaslov.lumic.cache.SensitivitySettingCache
import com.arturmaslov.lumic.cache.SensitivitySettingCacheImpl
import com.arturmaslov.lumic.utils.ActivityHelper
import com.arturmaslov.lumic.utils.AudioUtils
import com.arturmaslov.lumic.utils.CameraUtils
import com.arturmaslov.lumic.utils.FlashMode
import com.arturmaslov.lumic.utils.LoadStatus
import com.arturmaslov.lumic.utils.PermissionStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.koin.android.ext.android.inject
import kotlin.getValue

open class BaseActivity: ComponentActivity(), ActivityHelper {

    val baseCameraPermissionStatus = MutableStateFlow(PermissionStatus.DENIED)
    val baseAudioRecordPermissionStatus = MutableStateFlow(PermissionStatus.DENIED)
    val baseLoadStatus = MutableStateFlow(LoadStatus.LOADING)
    val baseRequestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
//            val permissionKeys = permissions.keys.map { it }
            permissions.entries.forEachIndexed { index, entry ->
                when (entry.key) {
                    Manifest.permission.CAMERA -> {
                        baseCameraPermissionStatus.value =
                            if (entry.value) {
                                PermissionStatus.GRANTED
                            } else {
                                PermissionStatus.DENIED
                            }
                    }

                    Manifest.permission.RECORD_AUDIO -> {
                        baseAudioRecordPermissionStatus.value =
                            if (entry.value) {
                                PermissionStatus.GRANTED
                            } else {
                                PermissionStatus.DENIED
                            }
                    }
                }
            }
        }
    val baseFlashMode = MutableStateFlow(FlashMode.BOTH)

    val sensitivitySettingsCache by inject<SensitivitySettingCache>()
    val colorSettingsCache by inject<ColorSettingCache>()
    val flashSettingsCache by inject<FlashSettingCache>()

    override fun setListeners() {}
    override fun setObservers() {}
    override fun requestPermissions(permissionArray: Array<String>) {}

    fun cameraPermissionStatus() = baseCameraPermissionStatus as StateFlow<PermissionStatus>
    fun audioRecordPermissionStatus() =
        baseAudioRecordPermissionStatus as StateFlow<PermissionStatus>
    fun loadStatus() = baseLoadStatus as StateFlow<LoadStatus>

    companion object {}
}