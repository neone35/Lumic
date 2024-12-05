package com.arturmaslov.lumic.data

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RoomDatabase

@Database(
    entities = [
        UserSettingEntity::class,
    ], version = 1
)
abstract class LocalDatabase : RoomDatabase() {
    // The associated DAOs for the database
    abstract val userSettingDao: UserSettingDao?

    companion object {
        const val DATABASE_NAME = "LUMIC_DB"
    }
}

@Dao
interface UserSettingDao {
    @Query("SELECT * FROM userSettingEntity")
    fun getAllUsersSettings(): List<UserSettingEntity>?

    @Query("SELECT * FROM userSettingEntity WHERE id = :id")
    fun getUserSetting(id: Int): UserSettingEntity

    @Query("SELECT * FROM userSettingEntity WHERE userName = :userName")
    fun getOneUserSettings(userName: String): List<UserSettingEntity>?

    // returns row id of inserted item
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUserSetting(userSetting: UserSettingEntity): Long

    // returns number of rows affected
    @Query("DELETE FROM userSettingEntity")
    fun deleteAllUserSettings(): Int

    // returns number of rows affected
    @Query("DELETE FROM userSettingEntity WHERE userName = :userName")
    fun deleteOneUserSettings(userName: String): Int

}