package com.arturmaslov.lumic.data

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import timber.log.Timber

class LocalDataSource(
    mLocalDatabase: LocalDatabase,
    private val mDispatcher: CoroutineDispatcher
) : LocalData {

    private val userSettingDao: UserSettingDao? = mLocalDatabase.userSettingDao

    override suspend fun getAllUsersSettings() =
        withContext(mDispatcher) {
            Timber.i("Running getAllUsersSettings()")
            val allUserSettingEntities = userSettingDao?.getAllUsersSettings()
            val allUserSettings = allUserSettingEntities?.map { it.toDomainModel() }
            if (allUserSettings != null) {
                Timber.i("Success: user settings retrieved: $allUserSettings")
            } else {
                Timber.i("Failure: unable to retrieve all user settings")
            }
            allUserSettings
        }

    override suspend fun getUserSetting(id: Int) =
        withContext(mDispatcher) {
            Timber.i("Running getUserSetting()")
            val userSettingEntity = userSettingDao?.getUserSetting(id)
            val userSetting = userSettingEntity?.toDomainModel()
            if (userSetting != null) {
                Timber.i("Success: user settings $userSetting retrieved")
            } else {
                Timber.i("Failure: unable to retrieve user setting")
            }
            userSetting
        }

    override suspend fun getOneUserSettings(userName: String) =
        withContext(mDispatcher) {
            Timber.i("Running getOneUserSettings()")
            val oneUserSettings = userSettingDao?.getOneUserSettings(userName)
            val userSettingList = oneUserSettings?.map { it.toDomainModel() }
            if (userSettingList != null) {
                Timber.i("Success: user settings for $userName retrieved")
            } else {
                Timber.i("Failure: unable to retrieve settings for $userName")
            }
            userSettingList
        }


    override suspend fun deleteAllUserSettings() =
        withContext(mDispatcher) {
            Timber.i("Running deleteAllUserSettings()")
            val deletedRows = userSettingDao?.deleteAllUserSettings()
            if (deletedRows != 0) {
                Timber.i("Success: all local user setting data deleted")
            } else {
                Timber.i("Failure: unable to delete local user setting data")
            }
            deletedRows
        }

    override suspend fun deleteOneUserSettings(userName: String): Int? =
        withContext(mDispatcher) {
            Timber.i("Running deleteOneUserSettings()")
            val deletedRows = userSettingDao?.deleteOneUserSettings(userName)
            if (deletedRows != 0) {
                Timber.i("Success: settings of user $userName deleted")
            } else {
                Timber.i("Failure: unable to delete settings of user $userName")
            }
            deletedRows
        }

    override suspend fun insertUserSetting(userSetting: UserSetting): Long? =
        withContext(mDispatcher) {
            Timber.i("Running insertUserSetting()")
            val insertedId = userSettingDao?.insertUserSetting(userSetting.toEntity())
            if (insertedId != null) {
                Timber.i("Success: user setting for ${userSetting.userName} inserted")
            } else {
                Timber.i("Failure: unable to insert setting for ${userSetting.userName}")
            }
            insertedId
        }

}

interface LocalData {
    suspend fun getAllUsersSettings(): List<UserSetting>?
    suspend fun getUserSetting(id: Int): UserSetting?
    suspend fun getOneUserSettings(userName: String): List<UserSetting>?
    suspend fun deleteAllUserSettings(): Int?
    suspend fun deleteOneUserSettings(userName: String): Int?
    suspend fun insertUserSetting(userSetting: UserSetting): Long?
}