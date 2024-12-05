package com.arturmaslov.lumic.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.arturmaslov.lumic.utils.FlashMode

@Entity
data class UserSettingEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Int? = null,
    val userName: String? = null,
    val colorSetting: Int? = null,
    val flashDuration: Float? = null,
    val flashMode: FlashMode? = null,
    val sensitivity: Float? = null,
)

data class UserSetting(
    val id: Int? = null,
    val userName: String? = null,
    val colorSetting: Int? = null,
    val flashDuration: Float? = null,
    val flashMode: FlashMode? = null,
    val sensitivity: Float? = null,
)

fun UserSetting.toEntity(): UserSettingEntity {
    return UserSettingEntity(
        id = id,
        userName = userName,
        colorSetting = colorSetting,
        flashDuration = flashDuration,
        flashMode = flashMode,
        sensitivity = sensitivity
    )
}

fun UserSettingEntity.toDomainModel(): UserSetting {
    return UserSetting(
        id = id,
        userName = userName,
        colorSetting = colorSetting,
        flashDuration = flashDuration,
        flashMode = flashMode,
        sensitivity = sensitivity
    )
}