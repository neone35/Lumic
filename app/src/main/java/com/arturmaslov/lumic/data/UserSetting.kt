package com.arturmaslov.lumic.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.arturmaslov.lumic.utils.FlashMode

@Entity
data class UserSettingEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val userId: Int,
    val colorSetting: Int,
    val flashDuration: Float,
    val flashMode: FlashMode,
    val sensitivity: Float,
)

data class UserSetting(
    val id: Int? = null,
    val userId: Int? = null,
    val colorSetting: Int? = null,
    val flashDuration: Float? = null,
    val flashMode: FlashMode? = null,
    val sensitivity: Float? = null,
)

fun UserSetting.toEntity(): UserSettingEntity {
    return UserSettingEntity(
        id = id ?: 0,
        userId = userId ?: 0,
        colorSetting = colorSetting ?: 0,
        flashDuration = flashDuration ?: 0f,
        flashMode = flashMode ?: FlashMode.NONE,
        sensitivity = sensitivity ?: 0f
    )
}

fun UserSettingEntity.toDomainModel(): UserSetting {
    return UserSetting(
        id = id,
        userId = userId,
        colorSetting = colorSetting,
        flashDuration = flashDuration,
        flashMode = flashMode,
        sensitivity = sensitivity
    )
}